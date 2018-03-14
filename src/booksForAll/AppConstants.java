package booksForAll;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;

import booksForAll.model.Customer;
import booksForAll.model.Book;
import booksForAll.model.Like;
import booksForAll.model.Review;
import booksForAll.model.BooksToUser;
import booksForAll.model.Purchase;

/**
 * A simple place to hold global application constants
 */
public interface AppConstants {

	public final String CUSTOMERS = "customers";
	public final String CUSTOMERS_FILE = CUSTOMERS + ".json";
	public final String NAME = "name";
	public final Type CUSTOMER_COLLECTION = new TypeToken<Collection<Customer>>() {}.getType();
	
	public final String BOOKS = "books";
	public final String BOOKS_FILE = BOOKS + ".json";
	public final Type BOOK_COLLECTION = new TypeToken<Collection<Book>>() {}.getType();
	
	public final String LIKES = "likes";
	public final String LIKES_FILE = LIKES + ".json";
	public final Type LIKE_COLLECTION = new TypeToken<Collection<Like>>() {}.getType();
	
	public final String REVIEWS = "reviews";
	public final String REVIEWS_FILE = REVIEWS + ".json";
	public final Type REVIEW_COLLECTION = new TypeToken<Collection<Review>>() {}.getType();
	
	public final String BOOKSTOUSERS = "BooksToUsers";
	public final String BOOKSTOUSERS_FILE = BOOKSTOUSERS + ".json";
	public final Type BOOKSTOUSER_COLLECTION = new TypeToken<Collection<BooksToUser>>() {}.getType();
	
	public final String PURCHASES = "Purchases";
	public final String PURCHASES_FILE = BOOKSTOUSERS + ".json";
	public final Type PURCHASE_COLLECTION = new TypeToken<Collection<Purchase>>() {}.getType();
	
	//derby constants
	public final String DB_NAME = "DB_NAME";
	public final String DB_DATASOURCE = "DB_DATASOURCE";
	public final String PROTOCOL = "jdbc:derby:"; 
	public final String OPEN = "Open";
	public final String SHUTDOWN = "Shutdown";
	
	//sql statements
	public final String CREATE_CUSTOMERS_TABLE = "CREATE TABLE CUSTOMER(Username varchar(10) PRIMARY KEY,"
			+ "Password varchar(8),"
			+ "Email varchar(100) UNIQUE,"
			+ "Address varchar(100),"
			+ "Phone INT,"
			+ "Nickname varchar(20) UNIQUE,"
			+ "Description varchar(50),"
			+ "Photo varchar(50))";
	public final String INSERT_CUSTOMER_STMT = "INSERT INTO CUSTOMER VALUES(?,?,?,?,?,?,?,?)";
	public final String SELECT_ALL_CUSTOMERS_STMT = "SELECT * FROM CUSTOMER";
	public final String SELECT_CUSTOMER_BY_NAME_STMT = "SELECT * FROM CUSTOMER "
			+ "WHERE Name=?";
	
	public final String CREATE_BOOKS_TABLE = "CREATE TABLE BOOKS (idBooks INT PRIMARY KEY,"
			+ "Name varchar(100),"
			+ "Author varchar(100),"
			+ "Photo varchar(100),"
			+ "Price REAL,"
			+ "Description varchar(100))";
	public final String INSERT_BOOK_STMT = "INSERT INTO BOOK VALUES(?,?,?,?,?,?)";
	public final String SELECT_ALL_BOOKS_STMT = "SELECT * FROM BOOK";
	public final String SELECT_BOOK_BY_NAME_STMT = "SELECT * FROM BOOK "
			+ "WHERE Name=?";
	
	public final String CREATE_LIKES_TABLE = "CREATE TABLE LIKES( IdBook INT PRIMARY KEY,"
			+ "Username varchar(10) PRIMARY KEY,"
			+ "isActive INT,"
			+ "dataSet TimeStamp)";
	
	public final String INSERT_LIKE_STMT = "INSERT INTO LIKE VALUES(?,?,?,?)";
	public final String SELECT_ALL_LIKES_STMT = "SELECT * FROM LIKE";
	public final String SELECT_LIKE_BY_NAME_STMT = "SELECT * FROM LIKE "
			+ "WHERE Name=?";
	
	public final String CREATE_REVIEWS_TABLE = "CREATE TABLE REVIEWS(IdBook INT PRIMARY KEY,"
			+ "Username varchar(10) PRIMARY KEY,"
			+ "dataWritten TimeStamp,"
			+ "dataApproved TimeStamp,"
			+ "isApproved INT,"
			+ "reviewText varchar(50))";
	
	public final String INSERT_REVIEW_STMT = "INSERT INTO REVIEW VALUES(?,?,?,?,?,?)";
	public final String SELECT_ALL_REVIEWS_STMT = "SELECT * FROM REVIEW";
	public final String SELECT_REVIEW_BY_NAME_STMT = "SELECT * FROM REVIEW "
			+ "WHERE Name=?";
	
	public final String CREATE_BOOKSTOUSERS_TABLE = "CREATE TABLE BOOKSTOUSERS(idBooksToUsers INT PRIMARY KEY,"
			+ "Username varchar(10),"
			+ "Review varchar(50))";
	
	public final String INSERT_BOOKSTOUSER_STMT = "INSERT INTO BOOKSTOUSER VALUES(?,?,?)";
	public final String SELECT_ALL_BOOKSTOUSERS_STMT = "SELECT * FROM BOOKSTOUSER";
	public final String SELECT_BOOKSTOUSER_BY_NAME_STMT = "SELECT * FROM BOOKSTOUSER "
			+ "WHERE Name=?";
	
	public final String CREATE_PURCHASES_TABLE = "CREATE TABLE PURCHASES(IdPurchased INT PRIMARY KEY,"
			+ "IdBook INT PRIMARY KEY,"
			+ "Username varchar(10) PRIMARY KEY,"
			+ "DataBought TimeStamp)";
	
	public final String INSERT_PURCHASE_STMT = "INSERT INTO PURCHASE VALUES(?,?,?,?)";
	public final String SELECT_ALL_PURCHASES_STMT = "SELECT * FROM PURCHASE";
	public final String SELECT_PURCHASE_BY_NAME_STMT = "SELECT * FROM PURCHASE "
			+ "WHERE Name=?";
	
}









































