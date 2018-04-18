package booksForAll.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import booksForAll.AppConstants;
import booksForAll.model.Customer;
import booksForAll.model.Review;

/**
 * Servlet implementation class ReviewServlet
 */
@WebServlet(description = "get, add, approve reviews", urlPatterns = { "/ReviewServlet/book/*", "/ReviewServlet/new",
		"/ReviewServlet/review/*" })
public class ReviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReviewServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * get new reviews. get reviews for book
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context
					.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();

			Collection<Review> reviewsResult = new ArrayList<Review>();

			String uri = request.getRequestURI();
			if (uri.indexOf("new") != -1) {
				// admin only -- test
				HttpSession mySession = request.getSession(false);
				if (mySession == null) {
					// session available
					response.getWriter().println("not logged in");
					response.setStatus(405);
					return;
				}
				if (mySession.getAttribute("isAdmin") == null) {
					response.getWriter().println("admin only");
					response.setStatus(405);
					return;
				}

				// is admin
				PreparedStatement stmt;
				try {
					stmt = conn.prepareStatement(AppConstants.SELECT_ALL_UN_APPROVED_REVIEWS);
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						reviewsResult.add(new Review(rs.getInt("IdReviews"), rs.getInt("IdBook"),
								rs.getString("Username"), rs.getLong("dateWritten"), rs.getLong("dateApproved"),
								rs.getInt("isApproved"), rs.getString("reviewText")));
					}
					rs.close();
					stmt.close();
				} catch (SQLException e) {
					getServletContext().log("Error while querying for reviews", e);
					response.sendError(500);// internal server error
				}

			} else if (uri.indexOf("book") != -1) {
				String bookId = uri.substring(uri.indexOf("book") + "book".length() + 1);
				if ((bookId == null) || bookId.isEmpty()) {
					response.getWriter().println("faulty form");
					response.setStatus(405);
					return;
				}
				PreparedStatement stmt;
				try {
					stmt = conn.prepareStatement(AppConstants.SELECT_ALL_APPROVED_REVIEWS_BY_BOOK_STMT);
					bookId = bookId.replaceAll("\\%20", " ");
					stmt.setString(1, bookId);
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						reviewsResult.add(new Review(rs.getInt("IdReviews"), rs.getInt("IdBook"),
								rs.getString("Username"), rs.getLong("dateWritten"), rs.getLong("dateApproved"),
								rs.getInt("isApproved"), rs.getString("reviewText")));
					}
					rs.close();
					stmt.close();
				} catch (SQLException e) {
					getServletContext().log("Error while querying for reviews", e);
					response.sendError(500);// internal server error
				}

			}
			conn.close();
			Gson gson = new Gson();
			// convert from customers collection to json
			String reviewsJsonResult = gson.toJson(reviewsResult, AppConstants.REVIEW_COLLECTION);
			response.addHeader("Content-Type", "application/json");
			PrintWriter writer = response.getWriter();
			writer.println(reviewsJsonResult);
			writer.close();
		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}
	}

	/**
	 * post new review for book
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession mySession = request.getSession(false);
		if (mySession == null) {
			// session available
			response.getWriter().println("not logged in");
			response.setStatus(405);
			return;
		}
		try {
			Gson gson = new Gson();
			Type reviewType = new TypeToken<Review>() {
			}.getType();
			Review review;
			try {
				review = gson.fromJson(request.getReader(), reviewType);
			} catch (Exception e) {
				/* failed to parse gson */
				response.getWriter().println("failed to parse json " + e);
				response.sendError(405);
				return;
			}
			if (!mySession.getAttribute("username").toString().equals(review.getUsername())) {
				response.getWriter().println("can post only as logged user");
				response.setStatus(405);
				return;
			}

			// obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context
					.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();

			String uri = request.getRequestURI();
			if (uri.indexOf("book") != -1) {
				String bookId = uri.substring(uri.indexOf("book") + "book".length() + 1);
				if ((bookId == null) || bookId.isEmpty()) {
					response.getWriter().println("faulty form");
					response.setStatus(405);
					return;
				}
				// >>
				try {
					// >>>
					PreparedStatement purStmt = conn
							.prepareStatement(AppConstants.SELECT_PURCHASE_BY_USERNAME_AND_BOOKID_STMT);
					purStmt.setString(1, review.getUsername());
					purStmt.setString(2, bookId);
					ResultSet purRs = purStmt.executeQuery();
					if (!purRs.next()) {
						// did not make purchase
						// remove failed
						response.getWriter().println("Failed to add review - did not purchase");
						response.setStatus(404);
						purRs.close();
						purStmt.close();
						return;
					} else {
						PreparedStatement stmt;
						stmt = conn.prepareStatement(AppConstants.INSERT_NEW_REVIEW_STMT);
						bookId = bookId.replaceAll("\\%20", " ");
						stmt.setString(1, bookId);
						stmt.setString(2, review.getUsername());
						stmt.setLong(3, System.currentTimeMillis());
						stmt.setString(4, review.getReviewText());
						int result = stmt.executeUpdate();
						if (result < 1) {
							response.getWriter().println("failed to insert review");
							response.setStatus(405);
						}
						response.setStatus(200);
						stmt.close();
					}
					purRs.close();
					purStmt.close();
				} catch (SQLException e) {
					getServletContext().log("Error while querying for reviews", e);
					response.sendError(500);// internal server error
				}

			}
			conn.close();

		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}

	}

	/**
	 * approve review
	 * 
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// admin only -- test
		HttpSession mySession = request.getSession(false);
		if (mySession == null) {
			// session available
			response.getWriter().println("not logged in");
			response.setStatus(405);
			return;
		}
		if (mySession.getAttribute("isAdmin") == null) {
			response.getWriter().println("admin only");
			response.setStatus(405);
			return;
		}

		// is admin
		try {
			// obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context
					.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();

			String uri = request.getRequestURI();
			if (uri.indexOf("review") != -1) {
				String reviewId = uri.substring(uri.indexOf("review") + "review".length() + 1);
				if ((reviewId == null) || reviewId.isEmpty()) {
					response.getWriter().println("faulty form");
					response.setStatus(405);
					return;
				}
				PreparedStatement stmt;
				try {
					stmt = conn.prepareStatement(AppConstants.APPROVE_REVIEW_BY_REVIEWID_STMT);
					reviewId = reviewId.replaceAll("\\%20", " ");
					stmt.setString(1, reviewId);
					int result = stmt.executeUpdate();
					if (result < 1) {
						// failed to approve
						response.getWriter().println("Failed to remove like");
						response.setStatus(405);
					}
					response.setStatus(200);
					stmt.close();
				} catch (SQLException e) {
					getServletContext().log("Error while approving reviews", e);
					response.sendError(500);// internal server error
				}

			}
			conn.close();

		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}
	}

}
