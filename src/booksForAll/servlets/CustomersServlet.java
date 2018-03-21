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
import booksForAll.model.Customer;
import booksForAll.model.Book;
import booksForAll.model.Like;
import booksForAll.model.Review;

/**
 * Servlet implementation class CustomersServlet1
 */
@WebServlet(
		description = "Servlet to return all customers", 
		urlPatterns = { 
				"/customers", 
		//		"/customers/name/*"
		})
public class CustomersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomersServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*
    	try {
    		
        	//obtain CustomerDB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();

    		Collection<Customer> customersResult = new ArrayList<Customer>(); 
    		String uri = request.getRequestURI();
    		if (uri.indexOf(AppConstants.NAME) != -1){//filter customer by specific name
    			String name = uri.substring(uri.indexOf(AppConstants.NAME) + AppConstants.NAME.length() + 1);
    			PreparedStatement stmt;
    			try {
    				stmt = conn.prepareStatement(AppConstants.SELECT_CUSTOMER_BY_NAME_STMT);
    				name = name.replaceAll("\\%20", " ");
    				stmt.setString(1, name);
    				ResultSet rs = stmt.executeQuery();
    				while (rs.next()){
    					//customersResult.add(new Customer(rs.getString(1),rs.getString(2),rs.getString(3)));
    				}
    				rs.close();
    				stmt.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for customers", e);
    	    		response.sendError(500);//internal server error
    			}
    		}else{
    			Statement stmt;
    			try {
    				stmt = conn.createStatement();
    				ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_CUSTOMERS_STMT);
    				while (rs.next()){
    					//customersResult.add(new Customer(rs.getString(1),rs.getString(2),rs.getString(3)));
    				}
    				rs.close();
    				stmt.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for customers", e);
    	    		response.sendError(500);//internal server error
    			}

    		}

    		conn.close();
    		
    		Gson gson = new Gson();
        	//convert from customers collection to json
        	String customerJsonResult = gson.toJson(customersResult, AppConstants.CUSTOMER_COLLECTION);
        	response.addHeader("Content-Type", "application/json");
        	PrintWriter writer = response.getWriter();
        	writer.println(customerJsonResult);
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
		String result ="";
	try {
    		
        	//obtain CustomerDB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		ArrayList<Customer> customers = new ArrayList<Customer>();
    		ArrayList<Book> books = new ArrayList<Book>();
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
				stmt = conn.prepareStatement(AppConstants.SELECT_ALL_LIKES_STMT);
				rs = stmt.executeQuery();
				while (rs.next()){
					likes.add(new Like(rs.getString(2), rs.getString(3), rs.getBoolean(4), rs.getTimestamp(5)));
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
				stmt = conn.prepareStatement(AppConstants.SELECT_ALL_CUSTOMERS_STMT);
				rs = stmt.executeQuery();
				while (rs.next()){
					customers.add(new Customer(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)));
				}
				if(customers.isEmpty()) {
					result = "No Users";
		    		JsonObject json = new JsonObject();
		    		json.addProperty("Result", result);
					response.getWriter().println(json.toString());
		        	response.getWriter().close();
		        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		        	return;
				}
				for(Customer customer : customers) {
					customer.setBooks(GetUserBooks(customer.getUsername(), conn, books));
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				getServletContext().log("Error", e);
	    		response.sendError(500);//internal server error
    		}

    		conn.close();
    		Gson gson = new GsonBuilder()
    				.setDateFormat("yyyy-MM-dd HH:mm:ss.S")
    				.create();
    		JsonArray jsonUsers = new JsonArray();
    		
    		for (Customer user : customers) {
    			jsonUsers.add(gson.toJsonTree(user));
    		}
    		
    		response.addHeader("Content-Type", "application/json");
    		JsonObject json = new JsonObject();
    		json.addProperty("Result", result);
    		json.add("UsersList", jsonUsers);
    		response.getWriter().println(json.toString());
        	response.getWriter().close();
        	response.setStatus(HttpServletResponse.SC_OK);
    	} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}
	
	public ArrayList<Book> GetUserBooks(String username, Connection conn, ArrayList<Book> books){
		PreparedStatement stmt;
		ArrayList<Book> userBooks = new ArrayList<Book>();
		try {
			stmt = conn.prepareStatement(AppConstants.SELECT_PURCHASE_BY_CUSTOMER_STMT);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				for(Book book : books) {
					if(rs.getString(3).equals(book.getBookName())) {
						userBooks.add(book);
					}
				}
			}
			rs.close();				
			stmt.close();
		} catch (SQLException e) {
			getServletContext().log("Error", e);
		}
		return userBooks;
	}


}
