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

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;

import booksForAll.AppConstants;
import booksForAll.model.Book;

/**
 * Servlet implementation class BooksServlet1
 */
@WebServlet(
		description = "Servlet to provide details about books", 
		urlPatterns = { 
				"/books", 
				"/books/myBooks",
				"/books/bookId/*"
		})
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	try {
    		
        	//obtain ReviewDB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();

    		Collection<Book> booksResult = new ArrayList<Book>(); 
    		String uri = request.getRequestURI();
    		if (uri.indexOf("mybooks") != -1){//filter book by specific user
    			//TODO: get user name from session
    			String username = uri.substring(uri.indexOf("mybooks") + "mybooks".length() + 1);
    			PreparedStatement stmt;
    			try {
    				stmt = conn.prepareStatement(AppConstants.SELECT_BOOK_BY_USERNAME_STMT);
    				username = username.replaceAll("\\%20", " ");
    				stmt.setString(1, username);
    				ResultSet rs = stmt.executeQuery();
    				while (rs.next()){
    					booksResult.add(new Book(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getFloat(5),rs.getString(6),rs.getString(7)));
    				}
    				rs.close();
    				stmt.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for books", e);
    	    		response.sendError(500);//internal server error
    			}
    		}else if (uri.indexOf("bookId") != -1){//filter book by specific id{
    			String bookid = uri.substring(uri.indexOf("bookId") + "bookId".length() + 1);
    			PreparedStatement stmt;
    			try {
    				stmt = conn.prepareStatement(AppConstants.SELECT_BOOK_BY_BOOK_ID_STMT);
    				bookid = bookid.replaceAll("\\%20", " ");
    				stmt.setString(1, bookid);
    				ResultSet rs = stmt.executeQuery();
    				while (rs.next()){
    					booksResult.add(new Book(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getFloat(5),rs.getString(6),rs.getString(7)));
    				}
    				rs.close();
    				stmt.close();
    				//TODO: dont send file name
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for books", e);
    	    		response.sendError(500);//internal server error
    			}
			}else{
    			Statement stmt;
    			try {
    				stmt = conn.createStatement();
    				ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_BOOKS_STMT);
    				while (rs.next()){
    					booksResult.add(new Book(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getFloat(5),rs.getString(6),rs.getString(7)));
    				}
    				rs.close();
    				stmt.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for books", e);
    	    		response.sendError(500);//internal server error
    			}

    		}

    		conn.close();
    		
    		Gson gson = new Gson();
        	//convert from books collection to json
        	String bookJsonResult = gson.toJson(booksResult, AppConstants.REVIEW_COLLECTION);
        	response.addHeader("Content-Type", "application/json");
        	PrintWriter writer = response.getWriter();
        	writer.println(bookJsonResult);
        	writer.close();
    	} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}

    	
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
