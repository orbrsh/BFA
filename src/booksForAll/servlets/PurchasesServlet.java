package booksForAll.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
//import java.sql.PreparedStatement;
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
import booksForAll.model.Purchase;

/**
 * Servlet implementation class PurchasesServlet1
 */
@WebServlet(
		description = "Servlet to provide details about purchases", 
		urlPatterns = { 
				"/purchases"
		})
public class PurchasesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PurchasesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

    	
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
