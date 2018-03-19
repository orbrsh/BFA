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
import java.util.Collections;
import java.util.Comparator;
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
import com.google.gson.JsonObject;

import booksForAll.AppConstants;
import booksForAll.model.Book;
import booksForAll.model.Review;
import booksForAll.model.Like;
/**
 * Servlet implementation class BooksServlet1
 */
@WebServlet(
		description = "Servlet to provide details about all books", 
		urlPatterns = { 
				"/allbooks", 
		
		})
public class AllBooksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AllBooksServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
    /**
    * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
    */
    	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		// TODO Auto-generated method stub
    		String result = "";
    
    	try {
    		
        	//obtain DB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    		getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();

    		Collection<Book> books = new ArrayList<Book>(); 
    		List<Like> likes = new ArrayList<Like>();
    		List<Review> reviews = new ArrayList<Review>();
    		
     		PreparedStatement stmt;
     		try {
				stmt = conn.prepareStatement(AppConstants.SELECT_ALL_BOOKS_STMT);
				ResultSet rs = stmt.executeQuery();
				result = "Success";
				while (rs.next()){
					books.add(new Book(rs.getString(2),rs.getString(3),rs.getString(4),rs.getDouble(5),
							rs.getString(6), rs.getString(7), 0, null, null));
				}
				if(books.isEmpty()) {
					result = "Failure";
		    		JsonObject json = new JsonObject();
		    		json.addProperty("Result", result);
					response.getWriter().println(json.toString());
		        	response.getWriter().close();
		        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		        	return;
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
    		
    		Gson gson = new Gson();
        	//convert from books collection to json
        	String bookJsonResult = gson.toJson(books, AppConstants.BOOK_COLLECTION);
        	response.addHeader("Content-Type", "application/json");
        	PrintWriter writer = response.getWriter();
        	writer.println(bookJsonResult);
        	writer.close();
    	} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}

    	
    }

}
