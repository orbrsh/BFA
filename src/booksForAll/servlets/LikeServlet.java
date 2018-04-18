package booksForAll.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.Date;
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
import javax.xml.crypto.Data;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import booksForAll.AppConstants;
import booksForAll.model.Customer;
import booksForAll.model.Like;

/**
 * Servlet implementation class LikeServlet
 */
@WebServlet(description = "Ability to add / remove likes for pre-bought books. get all likes", urlPatterns = { "/LikeServlet/book/*" })
public class LikeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LikeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			// obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context
					.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();

			Collection<Like> likesResult = new ArrayList<Like>();
			String uri = request.getRequestURI();
			if (uri.indexOf("book") != -1) {// filter books by specific name
				String bookId = uri.substring(uri.indexOf("book") + "book".length() + 1);
				if ((bookId == null) || bookId.isEmpty()){
					response.getWriter().println("faulty form");
					response.setStatus(405);
					return;
				}
				PreparedStatement stmt;
				try {
					stmt = conn.prepareStatement(AppConstants.SELECT_ALL_LIKES_BY_BOOKID_STMT);
					bookId = bookId.replaceAll("\\%20", " ");
					stmt.setString(1, bookId);
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						likesResult.add(new Like(rs.getInt("IdBook"),rs.getString("Username"), rs.getLong("dateSetTimeStamp")));
					}
					rs.close();
					stmt.close();
				} catch (SQLException e) {
					getServletContext().log("Error while querying for likes", e);
					response.sendError(500);// internal server error
				}
			} /*else {
				// response error. only by name!
				getServletContext().log("not allowed to delete all customers");
				response.setStatus(405); // not-allowed : server error

			}*/

			conn.close();
			Gson gson = new Gson();
			// convert from customers collection to json
			String likesJsonResult = gson.toJson(likesResult, AppConstants.LIKE_COLLECTION);
			response.addHeader("Content-Type", "application/json");
			PrintWriter writer = response.getWriter();
			writer.println(likesJsonResult);
			writer.close();
		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}

		
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response)
		HttpSession mySession = request.getSession(false);
		if (mySession == null ) {
			// session not available
			response.getWriter().println("user not logged in");
			response.setStatus(405);
			return;
		}
//		Gson gson = new Gson();
//		Type likeType = new TypeToken<Like>() {
//		}.getType();
//		//Customer cust;
//		Like like;
//		try {
//			like = gson.fromJson(request.getReader(), likeType);
//			//response.getWriter().println(like.toString());
//		} catch (Exception e) {
//			/* failed to parse gson */
//			response.getWriter().println("failed to parse json " + e);
//			response.setStatus(405);
//			return;
//		}
		String username = mySession.getAttribute("username").toString();
		//int bookid = like.getIdBook();
		String uri = request.getRequestURI();
//		if (uri.indexOf("book") != -1) {// filter books by specific name
//			String bookId = uri.substring(uri.indexOf("book") + "book".length() + 1);
		
		
		if ((username == null) || (uri.indexOf("book") == -1) || username.isEmpty()) {
			response.getWriter().println("faulty form");
			response.setStatus(405);
			return;
		}
		String bookid;
		try {			
			bookid = uri.substring(uri.indexOf("book") + "book".length() + 1);
		}catch (Exception e) {
			response.getWriter().println("faulty form");
			response.setStatus(405);
			return;
		}
		
		if ((bookid == null) || bookid.isEmpty()){
			response.getWriter().println("faulty form");
			response.setStatus(405);
			return;
		}
		
		// check if liked already - then remove
			//SELECT_LIKE_BY_USERNNAME_BOOKID_STMT
		// check if user owns the book. then set like 
			//SELECT_PURCHASE_BY_USERNAME_AND_BOOKID_STMT
		
		// obtain CustomerDB data source from Tomcat's context
		try {
			Context context;
			context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context
					.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();

			PreparedStatement stmt;
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_LIKE_BY_USERNNAME_BOOKID_STMT);
				bookid = bookid.replaceAll("\\%20", " ");
				stmt.setString(1, username);
				stmt.setString(2, bookid);
				ResultSet rs = stmt.executeQuery();
				if(rs.next()) {
					//remove like
					PreparedStatement rmStmt = conn.prepareStatement(AppConstants.REMOVE_LIKE_STMT);
					rmStmt.setString(1, username);
					rmStmt.setString(2, bookid);
					int update = rmStmt.executeUpdate();
					if (update <1) {
						//remove failed
						response.getWriter().println("Failed to remove like");
						response.setStatus(405);
					}
					rmStmt.close();
					//return;
					
				} else {
					//not Liked
					// check purchase
					PreparedStatement purStmt = conn.prepareStatement(AppConstants.SELECT_PURCHASE_BY_USERNAME_AND_BOOKID_STMT);
					purStmt.setString(1, username);
					purStmt.setString(2, bookid);
					ResultSet purRs = purStmt.executeQuery();
					if(!purRs.next()) {
						// did not make purchase
						//remove failed
						response.getWriter().println("Failed to add like - did not purchase");
 						response.setStatus(404);
						purRs.close();
						purStmt.close();
						return;
					}else {
						// purchase ok, add like
						PreparedStatement likeStmt = conn.prepareStatement(AppConstants.INSERT_LIKE_STMT);
						likeStmt.setString(1, bookid);
						likeStmt.setString(2, username);
						likeStmt.setLong(3, System.currentTimeMillis());
						int insert = likeStmt.executeUpdate();
						if (insert<1) {
							response.getWriter().println("failed to add like");
							response.setStatus(405);
						}
						likeStmt.close();
					}
					purRs.close();
					purStmt.close();
				}
				
				rs.close();
				stmt.close();				
			} catch (SQLException e) {
				getServletContext().log("Error while querying for likes", e);
				response.sendError(500);// internal server error
			}
			conn.close();
			// response to server
		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}
	}

}
