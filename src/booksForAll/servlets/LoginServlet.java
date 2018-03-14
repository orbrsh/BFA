package booksForAll.servlets;

import java.io.IOException;
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

import booksForAll.AppConstants;
//import booksForAll.model.Customer;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
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
		// response.set
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
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String dbPassword;
		
		if ((username == null) || (password == null) ) {
			response.getWriter().println("faulty form");
			response.sendError(405);
			return;
		}
		
		if (username.isEmpty() || password.isEmpty()){
			response.getWriter().println("Wrong username / password (empty)");
			response.sendError(405);
			return;
		}
			

		if ((username.equals("admin")) && (password.equals("Passw0rd"))) {
			HttpSession mySession = request.getSession(true);
			mySession.setAttribute("username", username);
			mySession.setAttribute("isAdmin", true);
			response.setStatus(200);
			return;
		}

		// search db for username and matching password
		
		// obtain CustomerDB data source from Tomcat's context
		try {
			Context context;
			context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context
					.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();

			PreparedStatement stmt;
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_CUSTOMER_BY_NAME_STMT);
				//name = name.replaceAll("\\%20", " ");
				stmt.setString(1, username);
				ResultSet rs = stmt.executeQuery();
				if (!rs.next()) {
					//user not found
					return;
				}
				dbPassword = rs.getString("password");
//				while (rs.next()) {
//					customersResult.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3)));
//				}
				rs.close();
				stmt.close();
				if (password.equals(dbPassword)) {
					HttpSession mySession = request.getSession(true);
					mySession.setAttribute("username", username);
					//mySession.setAttribute("isAdmin", true);
					response.setStatus(200);
					return;
				} else {
					// wrong username or password
					response.getWriter().println("wrong username or password");
					response.sendError(405);
					return;
				}
			} catch (SQLException e) {
				getServletContext().log("Error while querying for customers", e);
				response.sendError(500);// internal server error
			}
			conn.close();
			// response to server
		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}
	}

}
