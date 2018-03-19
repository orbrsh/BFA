package booksForAll.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
//import java.sql.PreparedStatement;
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
import booksForAll.model.Book;
import booksForAll.model.Purchase;

/**
 * Servlet implementation class PurchasesServlet1
 */
@WebServlet(
		description = "Servlet to buy eBook", 
		urlPatterns = { 
				"/buyeBookServlet"
		})
public class buyeBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public buyeBookServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*
    	try {
    		
        	//obtain PurchaseDB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();

    		Collection<Purchase> purchasesResult = new ArrayList<Purchase>(); 
    		//String uri = request.getRequestURI();
    		
    			Statement stmt;
    			try {
    				stmt = conn.createStatement();
    				ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_PURCHASES_STMT);
    				while (rs.next()){
    					purchasesResult.add(new Purchase(rs.getInt(2),rs.getString(3),rs.getString(4),rs.getLong(5)));
    				}
    				rs.close();
    				stmt.close();
    			} catch (SQLException e) {
    				getServletContext().log("Error while querying for purchases", e);
    	    		response.sendError(500);//internal server error
    			}


    		conn.close();
    		
    		Gson gson = new Gson();
        	//convert from books collection to json
        	String purchaseJsonResult = gson.toJson(purchasesResult, AppConstants.REVIEW_COLLECTION);
        	response.addHeader("Content-Type", "application/json");
        	PrintWriter writer = response.getWriter();
        	writer.println(purchaseJsonResult);
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
			strBuf.append(line);
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd HH:mm:ss.S")
				.create();
		Data Data = gson.fromJson(strBuf.toString(), Data.class);
		
		String username = Data.Username;
		String bookname = Data.BookName;
		String result = "";
		try {
    		
        	//obtain CustomerDB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();

    		PreparedStatement stmt;
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_PURCHASES_BY_USER_AND_BOOK_NAME_STMT);
				stmt.setString(1, username);
				stmt.setString(2, bookname);
				ResultSet rs = stmt.executeQuery(); 
				if (rs.next()){
					result = "Book Already Purchased";
				}
				else {
					stmt = conn.prepareStatement(AppConstants.INSERT_PURCHASE_STMT);
					stmt.setString(1, username);
					stmt.setString(2, bookname);
					int res = stmt.executeUpdate();
					if (res != 0){
						result = "Success";
						stmt = conn.prepareStatement(AppConstants.SELECT_BOOK_BY_BOOK_NAME_STMT);
						stmt.setString(1, bookname);
						rs = stmt.executeQuery();
						Book book = null;
						if(rs.next()) {
							book = new Book(rs.getString(2),rs.getString(3),rs.getString(4),rs.getDouble(5),
							rs.getString(6), rs.getString(7), 0, null, null);						}

						//TODO: add to purchases data
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
    		response.getWriter().println(json.toString());
        	response.getWriter().close();
        	response.setStatus(HttpServletResponse.SC_OK);
    	} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
