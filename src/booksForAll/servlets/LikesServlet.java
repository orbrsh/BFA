package booksForAll.servlets;

import java.io.BufferedReader;
import java.io.IOException;
//import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
import java.util.ArrayList;
//import java.util.Collection;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import booksForAll.filters.Data;
import booksForAll.AppConstants;
import booksForAll.model.Like;
import booksForAll.model.Book;
import booksForAll.model.Review;

/**
 * Servlet implementation class ReviewsServlet1
 */
@WebServlet(
		description = "Servlet to provide details about likes", 
		urlPatterns = "/likes"
)
		
public class LikesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LikesServlet() {
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
    		// TODO Auto-generated method stub
    				StringBuffer strBuf = new StringBuffer();
    				BufferedReader reader = request.getReader();
    				String line = null;  
    				while ((line = reader.readLine()) != null)
    					strBuf.append(line);
    				Gson gson = new GsonBuilder()
    						.setDateFormat("yyyy-MM-dd HH:mm:ss.S")
    						.create();
    				Data Data = gson.fromJson(strBuf.toString(), Data.class);

    				String username = Data.Username;
    				String bookname = Data.BookName;
    				Book book = null;
    				String result = "";

    	try {
    		
        	//obtain LikeDB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();

    		PreparedStatement stmt;
    			try {
    				stmt = conn.prepareStatement(AppConstants.SELECT_LIKES_BY_CUSTOMER_AND_BOOK_NAME_STMT);
    				stmt.setString(1, username);
    				stmt.setString(2, bookname);
    				ResultSet rs = stmt.executeQuery(); 
    				if (rs.next()){
    					result = "Book Already Liked";
    				}
    				else {
    					stmt = conn.prepareStatement(AppConstants.INSERT_LIKE_STMT);
    					stmt.setString(1, username);
    					stmt.setString(2, bookname);
    					int res = stmt.executeUpdate(); 
    					if (res != 0){
    						result = "Success";
    						List<Like> likes = new ArrayList<Like>();
    			    		List<Review> reviews = new ArrayList<Review>();
    			    		stmt = conn.prepareStatement(AppConstants.SELECT_BOOK_BY_BOOK_ID_STMT);
    						stmt.setString(1, bookname);
    						rs = stmt.executeQuery(); 
    						if(rs.next()) {
    							book = new Book(rs.getString(2),rs.getString(3),rs.getString(4),rs.getDouble(5),
    									rs.getString(6), rs.getString(7), 0, null, null);
    							stmt = conn.prepareStatement(AppConstants.SELECT_LIKE_BY_BOOK_NAME_STMT);
    							stmt.setString(1, bookname);
    							rs = stmt.executeQuery();
    							while (rs.next()){
    								likes.add(new Like(rs.getString(2), rs.getString(3), rs.getBoolean(4), rs.getLong(5)));
    							}
    							stmt = conn.prepareStatement(AppConstants.SELECT_REVIEWS_BY_BOOK_NAME_STMT);
    							stmt.setString(1, bookname);
    							rs = stmt.executeQuery();
    							while (rs.next()){
    								reviews.add(new Review(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getTimestamp(4),rs.getInt(5),rs.getString(6)));
    							}
    							rs.close();
    							book.setLikes(likes);
    							book.setLikesNum(likes.size());
    							book.setReviews(reviews);
    						}
    					}
    					else {
    						result = "Failure";
    					}
    				}
    				stmt.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error", e);
    	    		response.sendError(500);//internal server error
        		}
    		conn.close();
    		
    		response.addHeader("Content-Type", "application/json");
    		JsonObject json = new JsonObject();
    		json.addProperty("Result", result);
    		json.add("Book", gson.toJsonTree(book));
    		response.getWriter().println(json.toString());
        	response.getWriter().close();
        	response.setStatus(HttpServletResponse.SC_OK);
    	} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}

    	}
}