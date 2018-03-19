package booksForAll.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import booksForAll.AppConstants;
import booksForAll.filters.Data;
import booksForAll.model.Book;
import booksForAll.model.Like;
import booksForAll.model.Purchase;
import booksForAll.model.Review;

/**
 * Servlet implementation class purchasesServlet1
 */
@WebServlet(
		description = "Servlet to provide list of Purchased books for user", 
		urlPatterns = { 
				"/UsersBooksServlet"
		})
public class UsersBooksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsersBooksServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*
    	try {
    		
        	//obtain UserPurchasesDB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();

    		Collection<Purchase> userPurchasesResult = new ArrayList<Purchase>(); 
    		String uri = request.getRequestURI();
    		if (uri.indexOf("userPurchases") != -1){//filter purchases by specific user
    			String username = uri.substring(uri.indexOf("userPurchases") + "userPurchases".length() + 1);
    			PreparedStatement stmt;
    			try {
    				stmt = conn.prepareStatement(AppConstants.SELECT_PURCHASE_BY_CUSTOMER_STMT);
    				username = username.replaceAll("\\%20", " ");
    				stmt.setString(1, username);
    				ResultSet rs = stmt.executeQuery();
    				while (rs.next()){
    					userPurchasesResult.add(new Purchase(rs.getInt(2),rs.getString(3),rs.getString(4),rs.getLong(5)));
    				}
    				rs.close();
    				stmt.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for purchases", e);
    	    		response.sendError(500);//internal server error
    			}
    		}
			else{
    			Statement stmt;
    			try {
    				stmt = conn.createStatement();
    				ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_PURCHASES_STMT);
    				while (rs.next()){
    					userPurchasesResult.add(new Purchase(rs.getInt(2),rs.getString(3),rs.getString(4),rs.getLong(5)));
    				}
    				rs.close();
    				stmt.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for purchases", e);
    	    		response.sendError(500);//internal server error
    			}

    		}

    		conn.close();
    		
    		Gson gson = new Gson();
        	//convert from userPurchases collection to json
        	String bookJsonResult = gson.toJson(userPurchasesResult, AppConstants.REVIEW_COLLECTION);
        	response.addHeader("Content-Type", "application/json");
        	PrintWriter writer = response.getWriter();
        	writer.println(bookJsonResult);
        	writer.close();
    	} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}

    	*/
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		StringBuffer strBuf = new StringBuffer();
		BufferedReader reader = request.getReader();
		String line = null;        
		while ((line = reader.readLine()) != null)
		{
			strBuf.append(line);
		}

		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd HH:mm:ss.S")
				.create();

		Data Data = gson.fromJson(strBuf.toString(), Data.class);
		String username = Data.Username;
		String result = "";
		try {
    		
        	//obtain DB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		ArrayList<Book> books = new ArrayList<Book>();
    		List<Like> likes = new ArrayList<Like>();
    		List<Review> reviews = new ArrayList<Review>();
    		PreparedStatement stmt;
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_PURCHASE_BY_CUSTOMER_STMT);
				stmt.setString(1, username);
				ResultSet rs = stmt.executeQuery(); 
				result = "Success";
				while (rs.next()){
					stmt = conn.prepareStatement(AppConstants.SELECT_BOOK_BY_BOOK_NAME_STMT);
					stmt.setString(1, rs.getString(3));
					ResultSet resSet = stmt.executeQuery(); 
					if(resSet.next()) {
						books.add(new Book(rs.getString(2),rs.getString(3),rs.getString(4),rs.getDouble(5),
								rs.getString(6), rs.getString(7), 0, null, null));
					}
				}
				if(books.isEmpty()) {
					result = "No Books";
				}
				stmt = conn.prepareStatement(AppConstants.SELECT_ALL_LIKES_STMT);
				rs = stmt.executeQuery();
				while (rs.next()){
					likes.add(new Like(rs.getString(2), rs.getString(3), rs.getBoolean(4), rs.getLong(5)));
				}
				stmt = conn.prepareStatement(AppConstants.SELECT_ALL_APPROVED_REVIEWS_STMT);
				rs = stmt.executeQuery();
				while (rs.next()){
					reviews.add(new Review(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getTimestamp(4),rs.getInt(5),rs.getString(6)));
				}
				
				//Match likes and reviews 
				for(Book book : books) {
					List<Like> bookLikes = new ArrayList<Like>();
					for(Like like : likes) {
						if(book.getBookName().compareTo((like.getBookName())) > 0) {
							bookLikes.add(like);
						}
					}
					book.setLikes(bookLikes);
					List<Review> bookReviews = new ArrayList<Review>();
					for(Review review : reviews) {
						if(book.getBookName().compareTo((review.getBookName())) > 0) {
							bookReviews.add(review);
						}
					}
	
					book.setReviews(bookReviews);
				}
				
				//Set for books the likes number according to the likes list in each book.
				for(Book book : books) {
					book.setLikesNum(book.getLikes().size());
				}
				
				rs.close();				
				stmt.close();
			} catch (SQLException e) {
				getServletContext().log("Error", e);
	    		response.sendError(500);//internal server error
    		}

    		conn.close();
    		JsonArray jsonBooks = new JsonArray();
    		for (Book book : books) {
    			jsonBooks.add(gson.toJsonTree(book));
    		}

        	response.addHeader("Content-Type", "application/json");
    		JsonObject json = new JsonObject();
    		json.addProperty("Result", result);
    		json.add("BookList", jsonBooks);
    		response.getWriter().println(json.toString());
        	response.getWriter().close();
        	response.setStatus(HttpServletResponse.SC_OK);
    	} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
