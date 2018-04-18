package booksForAll.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;

import booksForAll.AppConstants;
import booksForAll.model.Book;
import booksForAll.model.Purchase;

/**
 * Servlet implementation class PurchaseServlet
 */
@WebServlet(description = "Post purchase, get all purchases", urlPatterns = { "/Purchase" })
public class PurchaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PurchaseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//admin only -- test
		HttpSession mySession = request.getSession(false);
		if (mySession == null ) {
			// session available
			response.getWriter().println("not logged in");
			response.setStatus(405);
			return;
		}
		if (mySession.getAttribute("isAdmin") == null) {
			response.getWriter().println("admin only");
			response.setStatus(405);
			return;
		}
		
		// is admin
		
		try {

			// obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context
					.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();

			Collection<Purchase> purchaseResult = new ArrayList<Purchase>();
			PreparedStatement pstms = conn.prepareStatement(AppConstants.SELECT_ALL_PURCHASES_STMT);
			ResultSet rs = pstms.executeQuery();
			
			while (rs.next()) {
				Purchase purchase = new Purchase(rs.getInt("IdPurchase"), rs.getInt("IdBook"), rs.getString("Username"), rs.getLong("DateBought"));
				purchaseResult.add(purchase);
			}
			
			rs.close();
			pstms.close();
			
			conn.close();

			Gson gson = new Gson();
			// convert from purchases collection to json
			String purchaseJsonResult = gson.toJson(purchaseResult, AppConstants.PURCHASE_COLLECTION);
			response.addHeader("Content-Type", "application/json");
			PrintWriter writer = response.getWriter();
			writer.println(purchaseJsonResult);
			writer.close();
		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
