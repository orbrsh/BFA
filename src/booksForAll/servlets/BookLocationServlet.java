package booksForAll.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
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
import com.google.gson.reflect.TypeToken;

import booksForAll.AppConstants;
import booksForAll.model.BookLocation;
import booksForAll.model.Customer;
import booksForAll.model.Review;

/**
 * Servlet implementation class BookLocationServlet
 */
@WebServlet("/BookLocationServlet/book/*")
public class BookLocationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookLocationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession mySession = request.getSession(false);
		if (mySession == null ) {
			// session available
			response.getWriter().println("not logged in");
			response.setStatus(405);
			return;
		}
		try {

			// obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context
					.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();

			//Collection<Customer> customersResult = new ArrayList<Customer>();
			BookLocation locat = null;
			String uri = request.getRequestURI();
			if (uri.indexOf("book") != -1) {// filter customer by specific name
				String bookid = uri.substring(uri.indexOf("book") + "book".length() + 1);
				PreparedStatement stmt;
				try {
					stmt = conn.prepareStatement(AppConstants.SELECT_BOOK_LOCATION_STMT);
					bookid = bookid.replaceAll("\\%20", " ");
					stmt.setString(1, bookid);
					stmt.setString(2, mySession.getAttribute("username").toString());
					ResultSet rs = stmt.executeQuery();
					if (rs.next()) {
						locat = new BookLocation(rs.getInt("IdBook"), rs.getString("Username"), rs.getLong("location"));
					}
					rs.close();
					stmt.close();
				} catch (SQLException e) {
					getServletContext().log("Error while querying for customers", e);
					response.sendError(500);// internal server error
				}
			} 

			conn.close();

			Gson gson = new Gson();
			// convert from customers collection to json
			Type locationType = new TypeToken<BookLocation>() {}.getType();
			String locateJsonResult = gson.toJson(locat, locationType);
			response.addHeader("Content-Type", "application/json");
			PrintWriter writer = response.getWriter();
			writer.println(locateJsonResult);
			writer.close();
		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession mySession = request.getSession(false);
		if (mySession == null) {
			// session available
			response.getWriter().println("not logged in");
			response.setStatus(405);
			return;
		}
		try {
			Gson gson = new Gson();
			Type bookLocationType = new TypeToken<BookLocation>() {
			}.getType();
			BookLocation locat;
			try {
				locat = gson.fromJson(request.getReader(), bookLocationType);
			} catch (Exception e) {
				/* failed to parse gson */
				response.getWriter().println("failed to parse json " + e);
				response.sendError(405);
				return;
			}
			if (!mySession.getAttribute("username").toString().equals(locat.getUsername())) {
				response.getWriter().println("can save only as logged user");
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
				try {
					PreparedStatement locStmt = conn
							.prepareStatement(AppConstants.UPDATE_BOOK_LOCATION_STMT);
					locStmt.setLong(1, locat.getBookLocation());
					locStmt.setString(2, bookId);
					locStmt.setString(3, locat.getUsername());
					int result = locStmt.executeUpdate();
					if (result<1) {
						// did not make purchase
						// remove failed
						response.getWriter().println("Failed to update location");
						response.setStatus(404);
						locStmt.close();
						return;
					} 
					locStmt.close();
					response.setStatus(200);
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

}
