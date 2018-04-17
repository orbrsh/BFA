package booksForAll.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import booksForAll.AppConstants;
import booksForAll.model.Book;
import booksForAll.model.Customer;
import booksForAll.model.Like;
import booksForAll.model.Review;

/**
 * Servlet implementation class BooksServlet
 */
@WebServlet(description = "Book servlet - get books", urlPatterns = { "/BooksServlet/getAllBooks", "/BooksServlet/myBooks",
		"/BooksServlet/singleBook/*" })
public class BooksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BooksServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		try {

			// obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context
					.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();

			Collection<Book> booksResult = new ArrayList<Book>();
			String uri = request.getRequestURI();
			if (uri.indexOf("singleBook") != -1) {// filter customer by specific name
				String name = uri.substring(uri.indexOf("singleBook") + "singleBook".length() + 1);
				PreparedStatement stmt;
				try {
					stmt = conn.prepareStatement(AppConstants.SELECT_BOOK_BY_NAME_STMT);
					name = name.replaceAll("\\%20", " ");
					stmt.setString(1, name);
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						Book thisBook = new Book(rs.getInt("IdBooks"), rs.getString("Name"), 
										rs.getString("Author"), rs.getString("Photo"), 
										rs.getFloat("Price"), rs.getString("Description"), rs.getString("FullHtml"));
						int bookId = rs.getInt("IdBooks");
						PreparedStatement stmt2 = conn.prepareStatement(AppConstants.SELECT_ALL_LIKES_BY_BOOKID_STMT);
						stmt2.setInt(1, bookId);
						ResultSet rs2 = stmt2.executeQuery();
						Collection<Like> likes = new ArrayList<Like>();
						while (rs2.next()) {
							Like myLike = new Like(bookId, rs2.getString("Username"), rs2.getLong("dateSetTimeStamp"));
							likes.add(myLike);
						}
						rs2.close();
						stmt2.close();
						thisBook.setLikes(likes);
						
						
						stmt2 = conn.prepareStatement(AppConstants.SELECT_ALL_APPROVED_REVIEWS_BY_BOOK_STMT);
						stmt2.setInt(1, bookId);
						rs2 = stmt2.executeQuery();
						Collection<Review> reviews = new ArrayList<Review>();
						while (rs2.next()) {
							Review myReview = new Review(rs2.getInt("IdReviews"), bookId, rs2.getString("Username"), 
									rs2.getLong("dateWritten"), rs2.getLong("dateApproved"), rs2.getInt("isApproved"),
									rs2.getString("reviewText"));
							reviews.add(myReview);
						}
						rs2.close();
						stmt2.close();
						thisBook.setReviews(reviews);
						
						booksResult.add(thisBook);				
					}
					rs.close();
					stmt.close();
				} catch (SQLException e) {
					getServletContext().log("Error while querying for a book", e);
					response.sendError(500);// internal server error
				}
			} else if(uri.indexOf("myBooks") != -1)  {
				HttpSession mySession = request.getSession(false);
				if(mySession == null) {
					conn.close();
					response.setStatus(405);
					return;
				}
				String uname = mySession.getAttribute("username").toString();
				if(uname == null) {
					mySession.invalidate();
					conn.close();
					response.setStatus(405);
					return;
				}
				PreparedStatement stmt;
				PreparedStatement stmt2;
				try {
					Collection<Integer> myPurchasedBooks = new ArrayList<Integer>(); 
					stmt2 = conn.prepareStatement(AppConstants.SELECT_PURCHASE_BY_USERNAME_STMT);
					stmt2.setString(1, uname);
					ResultSet rs2 = stmt2.executeQuery();
					while (rs2.next()) {
						myPurchasedBooks.add(rs2.getInt("idBook"));
					}
					stmt2.close();
					rs2.close();
					
					for (int item: myPurchasedBooks) {
						stmt = conn.prepareStatement(AppConstants.SELECT_BOOK_BY_BOOKID_STMT);
						stmt.setInt(1, item);
						ResultSet rs = stmt.executeQuery();
						while (rs.next()) {
							Book thisBook = new Book(rs.getInt("IdBooks"), rs.getString("Name"), 
											rs.getString("Author"), rs.getString("Photo"), 
											rs.getFloat("Price"), rs.getString("Description"), rs.getString("FullHtml"));
							int bookId = rs.getInt("IdBooks");
							PreparedStatement stmt3 = conn.prepareStatement(AppConstants.SELECT_ALL_LIKES_BY_BOOKID_STMT);
							stmt3.setInt(1, bookId);
							ResultSet rs3 = stmt3.executeQuery();
							Collection<Like> likes = new ArrayList<Like>();
							while (rs3.next()) {
								Like myLike = new Like(bookId, rs3.getString("Username"), rs3.getLong("dateSetTimeStamp"));
								likes.add(myLike);
							}
							rs3.close();
							stmt3.close();
							thisBook.setLikes(likes);
							
							
							stmt3 = conn.prepareStatement(AppConstants.SELECT_ALL_APPROVED_REVIEWS_BY_BOOK_STMT);
							stmt3.setInt(1, bookId);
							rs3 = stmt3.executeQuery();
							Collection<Review> reviews = new ArrayList<Review>();
							while (rs3.next()) {
								Review myReview = new Review(rs3.getInt("IdReviews"), bookId, rs3.getString("Username"), 
										rs3.getLong("dateWritten"), rs3.getLong("dateApproved"), rs3.getInt("isApproved"),
										rs3.getString("reviewText"));
								reviews.add(myReview);
							}
							rs3.close();
							stmt3.close();
							thisBook.setReviews(reviews);
							
							booksResult.add(thisBook);				
						}
						rs.close();
						stmt.close();
					}
					
				} catch (SQLException e) {
					getServletContext().log("Error while querying for a book", e);
					response.sendError(500);// internal server error
				}
			}else if(uri.indexOf("getAllBooks") != -1){ // all books
				Statement stmt;
				try {
					stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_BOOKS_STMT);
					while (rs.next()) {
						Book thisBook = new Book(rs.getInt("IdBooks"), rs.getString("Name"), 
										rs.getString("Author"), rs.getString("Photo"), 
										rs.getFloat("Price"), rs.getString("Description"), rs.getString("FullHtml"));
						int bookId = rs.getInt("IdBooks");
						PreparedStatement stmt3 = conn.prepareStatement(AppConstants.SELECT_ALL_LIKES_BY_BOOKID_STMT);
						stmt3.setInt(1, bookId);
						ResultSet rs3 = stmt3.executeQuery();
						Collection<Like> likes = new ArrayList<Like>();
						while (rs3.next()) {
							Like myLike = new Like(bookId, rs3.getString("Username"), rs3.getLong("dateSetTimeStamp"));
							likes.add(myLike);
						}
						rs3.close();
						stmt3.close();
						thisBook.setLikes(likes);
						
						
						stmt3 = conn.prepareStatement(AppConstants.SELECT_ALL_APPROVED_REVIEWS_BY_BOOK_STMT);
						stmt3.setInt(1, bookId);
						rs3 = stmt3.executeQuery();
						Collection<Review> reviews = new ArrayList<Review>();
						while (rs3.next()) {
							Review myReview = new Review(rs3.getInt("IdReviews"), bookId, rs3.getString("Username"), 
									rs3.getLong("dateWritten"), rs3.getLong("dateApproved"), rs3.getInt("isApproved"),
									rs3.getString("reviewText"));
							reviews.add(myReview);
						}
						rs3.close();
						stmt3.close();
						thisBook.setReviews(reviews);
						
						booksResult.add(thisBook);				
					}
					rs.close();
					stmt.close();

				} catch (SQLException e) {
					getServletContext().log("Error while querying for customers", e);
					response.sendError(500);// internal server error
				}

			}else { // no proper input
				response.sendError(404);
			}

			conn.close();

			Gson gson = new Gson();
			// convert from customers collection to json
			String customerJsonResult = gson.toJson(booksResult, AppConstants.BOOK_COLLECTION);
			response.addHeader("Content-Type", "application/json");
			PrintWriter writer = response.getWriter();
			writer.println(customerJsonResult);
			writer.close();
		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
	}

}
