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
import booksForAll.model.Purchase;

/**
 * Servlet implementation class purchasesServlet1
 */
@WebServlet(
		description = "Servlet to provide details about userPurchases", 
		urlPatterns = { 
				"/userPurchases"
		})
public class UserPurchasesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserPurchasesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
    				stmt = conn.prepareStatement(AppConstants.SELECT_PURCHASE_BY_USER_STMT);
    				username = username.replaceAll("\\%20", " ");
    				stmt.setString(1, username);
    				ResultSet rs = stmt.executeQuery();
    				while (rs.next()){
    					userPurchasesResult.add(new Purchase(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getLong(4)));
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
    					userPurchasesResult.add(new Purchase(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getLong(4)));
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
