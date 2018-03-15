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

			// insert row
			try {
				PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_CUSTOMER_STMT);
				pstmt.setString(1, cust.getUsername());
				pstmt.setString(2, cust.getPassword());
				pstmt.setString(3, cust.getMail());
				pstmt.setString(4, cust.getAddress());
				pstmt.setString(5, cust.getPhoto());
				pstmt.setString(6, cust.getNickname());
				pstmt.setString(7, cust.getDescription());
				pstmt.setInt(8, cust.getPhone());
				pstmt.executeUpdate();

				// commit updates
				conn.commit();
				// close statements
				pstmt.close();

				// close connection
				conn.close();
				HttpSession mySession = request.getSession(true);
				mySession.setAttribute("username", cust.getUsername());
				
			} catch (SQLException e) {
				getServletContext().log("Error while querying for customers", e);
				response.sendError(500);// internal server error
			}

		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}
		
		response.getWriter().println("User "+cust.getUsername() + " registered correctly and now logged in");
		// response.getWriter().println(cust);
		response.setStatus(200);

	}

}
