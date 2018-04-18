package booksForAll.servlets;

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

import booksForAll.AppConstants;
//import booksForAll.model.Customer;
import booksForAll.model.Customer;

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
		HttpSession mySession = request.getSession(false);
		if (mySession != null ) {
			// session available
			String object = "{ \"username\": \"" +mySession.getAttribute("username")+ "\"";
			//boolean isAdmin = (boolean) mySession.getAttribute("isAdmin");
			if (mySession.getAttribute("isAdmin") != null) {
				object += ", \"isAdmin\": true ";
			}
			object += "}";
			response.getWriter().println(object);
			response.setContentType("application/json");
			response.setStatus(200);
			return;
		}else {
			response.getWriter().println("not logged in");
			response.setStatus(404);
		}
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		//response.sendError(405);
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
		String username = cust.getUsername(); //request.getParameter("username");
		String password = cust.getPassword(); //request.getParameter("password");
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
					response.getWriter().println("wrong username or password");
					response.setStatus(405);
					return;
				}
				if (rs.getInt("isActive") == 0) { //TODO: test
					//user deleted
					response.getWriter().println("user deleted");
					response.setStatus(405);
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
