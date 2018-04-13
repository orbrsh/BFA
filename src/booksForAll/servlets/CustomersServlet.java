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
import booksForAll.model.Customer;

/**
 * Servlet implementation class CustomersServlet1
 */
@WebServlet(description = "Servlet to provide details about customers", urlPatterns = { "/customers",
		"/customers/name/*" })
public class CustomersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CustomersServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// super.doDelete(req, resp);
		try {

			// obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context
					.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();

			//Collection<Customer> customersResult = new ArrayList<Customer>();
			String uri = request.getRequestURI();
			if (uri.indexOf(AppConstants.NAME) != -1) {// filter customer by specific name
				String name = uri.substring(uri.indexOf(AppConstants.NAME) + AppConstants.NAME.length() + 1);
				PreparedStatement stmt;
				try {
					stmt = conn.prepareStatement(AppConstants.REMOVE_CUSTOMER_BY_NAME_STMT);
					name = name.replaceAll("\\%20", " ");
					stmt.setString(1, name);
					int result = stmt.executeUpdate();
					if (result < 1) {
						// Unsuccessful 
						response.getWriter().println("failed to remove user. i.e. not found");
						response.setStatus(404);
					}

					stmt.close();
				} catch (SQLException e) {
					getServletContext().log("Error while querying for customers", e);
					response.sendError(500);// internal server error
				}
			} else {
				// response error. only by name!
				getServletContext().log("not allowed to delete all customers");
				response.setStatus(405); // not-allowed : server error

			}

			conn.close();

		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}

	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			// obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context
					.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();

			Collection<Customer> customersResult = new ArrayList<Customer>();
			String uri = request.getRequestURI();
			if (uri.indexOf(AppConstants.NAME) != -1) {// filter customer by specific name
				String name = uri.substring(uri.indexOf(AppConstants.NAME) + AppConstants.NAME.length() + 1);
				PreparedStatement stmt;
				try {
					stmt = conn.prepareStatement(AppConstants.SELECT_CUSTOMER_BY_NAME_STMT);
					name = name.replaceAll("\\%20", " ");
					stmt.setString(1, name);
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						customersResult.add(new Customer(rs.getString("Username"), rs.getString("Address"),
								rs.getString("Nickname"), rs.getString("Description"), rs.getString("Photo"),
								rs.getString("Email"), rs.getInt("Phone")));
					}
					rs.close();
					stmt.close();
				} catch (SQLException e) {
					getServletContext().log("Error while querying for customers", e);
					response.sendError(500);// internal server error
				}
			} else {
				Statement stmt;
				try {
					// TODO: only admin
					stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_ACTIVE_CUSTOMERS_STMT);
					while (rs.next()) {
						customersResult.add(new Customer(rs.getString("Username"), rs.getString("Address"),
								rs.getString("Nickname"), rs.getString("Description"), rs.getString("Photo"),
								rs.getString("Email"), rs.getInt("Phone")));
					}
					rs.close();
					stmt.close();
				} catch (SQLException e) {
					getServletContext().log("Error while querying for customers", e);
					response.sendError(500);// internal server error
				}

			}

			conn.close();

			Gson gson = new Gson();
			// convert from customers collection to json
			String customerJsonResult = gson.toJson(customersResult, AppConstants.CUSTOMER_COLLECTION);
			response.addHeader("Content-Type", "application/json");
			PrintWriter writer = response.getWriter();
			writer.println(customerJsonResult);
			writer.close();
		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
