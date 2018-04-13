package booksForAll.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import booksForAll.AppConstants;
import booksForAll.model.Customer;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet(description = "Registration servlet, responds to POST only", urlPatterns = { "/RegistrationServlet" })
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegistrationServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		response.sendError(405);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		if (request.getSession(false) != null ) {
			// session available
			response.getWriter().println("already logged in");
			response.sendError(405);
			return;
		}
		
		Gson gson = new Gson();
		Type custType = new TypeToken<Customer>() {
		}.getType();
		Customer cust;
		try {
			cust = gson.fromJson(request.getReader(), custType);
			response.getWriter().println(cust.toString());
		} catch (Exception e) {
			/* failed to parse gson */
			response.getWriter().println("failed to parse json " + e);
			response.sendError(405);
			return;
		}

		try {
			// connect to DB for insertion
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context
					.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			// check not already registered
			PreparedStatement stmt;
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_CUSTOMER_BY_NAME_EMAIL_NICK_STMT);
				//name = name.replaceAll("\\%20", " ");
				stmt.setString(1, cust.getUsername());
				stmt.setString(2, cust.getMail());
				stmt.setString(3, cust.getNickname());
				ResultSet rs = stmt.executeQuery();
				
				boolean found = false;
				
				if (rs.next()) {
					// duplicate found
					found = true;
				}
//				while (rs.next()){
//					//customersResult.add(new Customer(rs.getString(1),rs.getString(2),rs.getString(3)));
//				}
				rs.close();
				stmt.close();
				if (found) {
					conn.close();
					response.getWriter().println("user already registered");
					response.setStatus(405);
					return;
				}
			} catch (SQLException e) {
				getServletContext().log("Error while querying for customers", e);
				response.sendError(500);// internal server error
			}
			

			// insert row
			try {
				PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_CUSTOMER_STMT);
				pstmt.setString(1,cust.getUsername());
				pstmt.setString(2,cust.getPassword());
				pstmt.setString(3,cust.getAddress());
				pstmt.setString(4,cust.getNickname());
				pstmt.setString(5,cust.getDescription());
				pstmt.setString(6,cust.getPhoto());
				pstmt.setString(7, cust.getMail());
				pstmt.setInt(8,cust.getPhone());
				pstmt.executeUpdate();

				// commit updates
				conn.commit();
				// close statements
				pstmt.close();

				HttpSession mySession = request.getSession(true);
				mySession.setAttribute("username", cust.getUsername());
				
			} catch (SQLException e) {
				getServletContext().log("Error while querying for customers", e);
				response.sendError(500);// internal server error
			}
			
			// close connection
			conn.close();

		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}
		
		response.getWriter().println("User "+cust.getUsername() + " registered correctly and now logged in");
		// response.getWriter().println(cust);
		response.setStatus(200);

	}

}
