package booksForAll.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
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
import com.google.gson.reflect.TypeToken;

import booksForAll.AppConstants;
import booksForAll.model.Book;
import booksForAll.model.Customer;
import booksForAll.model.Purchase;

/**
 * Servlet implementation class PurchaseServlet
 */
@WebServlet(description = "Post purchase, get all purchases", urlPatterns = { "/Purchase", "/Purchase/book/*" })
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
		// only logged users
		HttpSession mySession = request.getSession(false);
		if (mySession == null ) {
			// session available
			response.getWriter().println("not logged in");
			response.setStatus(405);
			return;
		}
		
		//String username = mySession.getAttribute("username").toString();
		
		// TODO accept also visa stuff
		
//		Gson gson = new Gson();
//		Type purchaseType = new TypeToken<Purchase>() {
//		}.getType();
//		Purchase purchase;
//		try {
//			purchase = gson.fromJson(request.getReader(), purchaseType);
//			//response.getWriter().println(cust.toString());
//		} catch (Exception e) {
//			/* failed to parse gson */
//			response.getWriter().println("failed to parse json " + e);
//			response.setStatus(405);
//			return;
//		}
//		if (!username.equals(purchase.getUsername())){
//			response.getWriter().println("can't buy for other user");
//			response.setStatus(405);
//			return;
//		}
//		purchase.setDateBought(System.currentTimeMillis());
		
		String username = mySession.getAttribute("username").toString();
		String uri = request.getRequestURI();
		
		if ((username == null) || (uri.indexOf("book") == -1) || username.isEmpty()) {
			response.getWriter().println("faulty form");
			response.setStatus(405);
			return;
		}
		String bookid;
		try {			
			bookid = uri.substring(uri.indexOf("book") + "book".length() + 1);
		}catch (Exception e) {
			response.getWriter().println("faulty form");
			response.setStatus(405);
			return;
		}
		
		if ((bookid == null) || bookid.isEmpty()){
			response.getWriter().println("faulty form");
			response.setStatus(405);
			return;
		}
		
		
		
		try {
		Context context;
		context = new InitialContext();
		BasicDataSource ds = (BasicDataSource) context
				.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
		Connection conn = ds.getConnection();

		PreparedStatement stmt;
		
		stmt = conn.prepareStatement(AppConstants.INSERT_PURCHASE_STMT);
		bookid = bookid.replaceAll("\\%20", " ");
		stmt.setString(1, bookid);
		stmt.setString(2, username);
		stmt.setLong(3, System.currentTimeMillis());
		
		int result = stmt.executeUpdate();
		
		if (result<1) {
			response.getWriter().println("purchase failed");
			response.getWriter().println("can't buy for other user");
			response.setStatus(405);
		}
		
		response.setStatus(200);
		stmt.close();
		conn.close();
		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}
	}

}
