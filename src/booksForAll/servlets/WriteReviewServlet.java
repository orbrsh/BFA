package booksForAll.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import booksForAll.AppConstants;
import booksForAll.filters.Data;
import booksForAll.model.Review;

/**
 * Servlet implementation class ReviewsServlet1
 */
@WebServlet(
		description = "Servlet to provide details about reviews", 
		urlPatterns = { 
				"/WriteReviewServlet"
		})
public class WriteReviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WriteReviewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*
    	try {
    		
        	//obtain ReviewDB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();

    		Collection<Review> reviewsResult = new ArrayList<Review>(); 
    		String uri = request.getRequestURI();
    		if (uri.indexOf("book") != -1){//filter review by specific name
    			String bookid = uri.substring(uri.indexOf("book") + "book".length() + 1);
    			PreparedStatement stmt;
    			try {
    				stmt = conn.prepareStatement(AppConstants.SELECT_REVIEW_BY_BOOK_ID_STMT);
    				bookid = bookid.replaceAll("\\%20", " ");
    				stmt.setString(1, bookid);
    				ResultSet rs = stmt.executeQuery();
    				while (rs.next()){
    					reviewsResult.add(new Review(rs.getString(2),rs.getString(3),rs.getLong(4),rs.getLong(5),rs.getInt(6),rs.getString(7)));
    				}
    				rs.close();
    				stmt.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for reviews", e);
    	    		response.sendError(500);//internal server error
    			}
    		}else{
    			Statement stmt;
    			try {
    				stmt = conn.createStatement();
    				ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_REVIEWS_STMT);
    				while (rs.next()){
    					reviewsResult.add(new Review(rs.getString(2),rs.getString(3),rs.getLong(4),rs.getLong(5),rs.getInt(6),rs.getString(7)));
    				}
    				rs.close();
    				stmt.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for reviews", e);
    	    		response.sendError(500);//internal server error
    			}

    		}

    		conn.close();
    		
    		Gson gson = new Gson();
        	//convert from reviews collection to json
        	String reviewJsonResult = gson.toJson(reviewsResult, AppConstants.REVIEW_COLLECTION);
        	response.addHeader("Content-Type", "application/json");
        	PrintWriter writer = response.getWriter();
        	writer.println(reviewJsonResult);
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
		String bookname = Data.BookName;
		String text = Data.reviewText;
		Timestamp current = new Timestamp(System.currentTimeMillis());
		String result = "";
		try {
    		
        	//obtain DB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();

    		PreparedStatement stmt;
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_CUSTOMER_BY_NAME_STMT);
				stmt.setString(1, username);
				
				
				stmt = conn.prepareStatement(AppConstants.INSERT_REVIEW_STMT);
				stmt.setString(1, bookname);
				stmt.setString(2, username);
				stmt.setTimestamp(3, current);
				stmt.setInt(4, 0); //set isApproved to 0
				stmt.setString(5, text);
				
				int res = stmt.executeUpdate(); 
				if (res != 0){
					result = "Success";
				}
				else {
					result = "Failure";
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
    		response.getWriter().println(json.toString());
        	response.getWriter().close();
        	response.setStatus(HttpServletResponse.SC_OK);
    	} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
