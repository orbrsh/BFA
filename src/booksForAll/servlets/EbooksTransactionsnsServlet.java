package booksForAll.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
import booksForAll.model.Book;
import booksForAll.model.Purchase;

/**
 * Servlet implementation class AllTransactionsServlet
 */
@WebServlet("/EbooksTransactionsnsServlet")
//This class returns all the transactions in the system to the admin.
public class EbooksTransactionsnsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EbooksTransactionsnsServlet() {
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
    		ArrayList<Purchase> purchases = new ArrayList<Purchase>();
    		PreparedStatement stmt;
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_ALL_PURCHASES_STMT);
				ResultSet rs = stmt.executeQuery();
				result = "Success";
				while (rs.next()){
					purchases.add(new Purchase(rs.getString(2),rs.getString(3),rs.getTimestamp(4),rs.getDouble(5)));
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				getServletContext().log("Error", e);
	    		response.sendError(500);//internal server error
    		}

    		conn.close();
    		if(purchases.isEmpty()) {
    			result = "Empty";
    		}
    		Gson gson = new GsonBuilder()
    				.setDateFormat("yyyy-MM-dd HH:mm:ss.S")
    				.create();
    		JsonArray jsonPurchases = new JsonArray();
    		for (Purchase purchase : purchases) {
    			jsonPurchases.add(gson.toJsonTree(purchase));
    		}
    		
    		response.addHeader("Content-Type", "application/json");
    		JsonObject json = new JsonObject();
    		json.addProperty("Result", result);
    		json.add("UserPurchases", jsonPurchases);
    		response.getWriter().println(json.toString());
        	response.getWriter().close();
        	response.setStatus(HttpServletResponse.SC_OK);
    	} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
