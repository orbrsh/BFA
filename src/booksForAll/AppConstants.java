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

	public final String CUSTOMERS = "Customers";
	public final String CUSTOMERS_FILE = CUSTOMERS + ".json";
	public final String NAME = "name";
	public final Type CUSTOMER_COLLECTION = new TypeToken<Collection<Customer>>() {}.getType();
	
	public final String BOOKS = "Books";
	public final String BOOKS_FILE = BOOKS + ".json";
	public final Type BOOK_COLLECTION = new TypeToken<Collection<Book>>() {}.getType();
	
	public final String LIKES = "Likes";
	public final String LIKES_FILE = LIKES + ".json";
	public final Type LIKE_COLLECTION = new TypeToken<Collection<Like>>() {}.getType();
	
	public final String REVIEWS = "Reviews";
	public final String REVIEWS_FILE = REVIEWS + ".json";
	public final Type REVIEW_COLLECTION = new TypeToken<Collection<Review>>() {}.getType();
	
	public final String BOOKSTOUSERS = "BooksToUsers";
	public final String BOOKSTOUSERS_FILE = BOOKSTOUSERS + ".json";
	public final Type BOOKSTOUSER_COLLECTION = new TypeToken<Collection<BooksToUser>>() {}.getType();
	
	public final String PURCHASES = "Purchases";
	public final String PURCHASES_FILE = PURCHASES + ".json";
	public final Type PURCHASE_COLLECTION = new TypeToken<Collection<Purchase>>() {}.getType();
	
	//derby constants
	public final String DB_NAME = "DB_NAME";
	public final String DB_DATASOURCE = "DB_DATASOURCE";
	public final String PROTOCOL = "jdbc:derby:"; 
	public final String OPEN = "Open";
	public final String SHUTDOWN = "Shutdown";
	
	//sql statements
	public final String CREATE_CUSTOMERS_TABLE = "CREATE TABLE CUSTOMER(Username varchar(10),"
			+ "Password varchar(8),"
			+ "Address varchar(100),"
			+ "Nickname varchar(20),"
			+ "Description varchar(50),"
			+ "Photo varchar(50),"
			+ "Email varchar(100),"
			+ "Phone INT,"
			+ "PRIMARY KEY (Username),"
			+ "UNIQUE (Email,Nickname))";
	public final String INSERT_CUSTOMER_STMT = "INSERT INTO CUSTOMER VALUES(?,?,?,?,?,?,?,?)";
	public final String SELECT_ALL_CUSTOMERS_STMT = "SELECT * FROM CUSTOMER";
	public final String SELECT_CUSTOMER_BY_NAME_STMT = "SELECT * FROM CUSTOMER "
			+ "WHERE Name=?";
	
	public final String CREATE_BOOKS_TABLE = "CREATE TABLE BOOKS (IdBooks INT,"
			+ "Name varchar(100),"
			+ "Author varchar(100),"
			+ "Photo varchar(100),"
			+ "Price REAL,"
			+ "Description varchar(300),"
			+ "FullHtml varchar(300),"
			+ "PRIMARY KEY (IdBooks))";
	public final String INSERT_BOOK_STMT = "INSERT INTO BOOKS VALUES(?,?,?,?,?,?,?)";
	public final String SELECT_ALL_BOOKS_STMT = "SELECT * FROM BOOKS";
	public final String SELECT_BOOK_BY_USERNAME_STMT = "SELECT * FROM BOOKS "
			+ "WHERE username=?";
	public final String SELECT_BOOK_BY_BOOK_ID_STMT = "SELECT * FROM BOOKS "
			+ "WHERE bookid=?";
	
	public final String CREATE_LIKES_TABLE = "CREATE TABLE LIKES( IdBook INT,"
			+ "Username varchar(10),"
			+ "isActive INT,"
			+ "dateSetTimeStamp BIGINT,"
			+ "PRIMARY KEY (IdBook, Username))";
	
	public final String INSERT_LIKE_STMT = "INSERT INTO LIKES VALUES(?,?,?,?)";
	public final String SELECT_ALL_LIKES_STMT = "SELECT * FROM LIKES";
	public final String SELECT_LIKE_BY_NAME_STMT = "SELECT * FROM LIKES"
			+ "WHERE Name=?";
	
	public final String CREATE_REVIEWS_TABLE = "CREATE TABLE REVIEWS(IdBook INT,"
			+ "Username varchar(10),"
			+ "dateWritten BIGINT,"
			+ "dateApproved BIGINT,"
			+ "isApproved INT,"
			+ "reviewText varchar(50),"
			+ "PRIMARY KEY(IdBook, Username))";
	
	public final String INSERT_REVIEW_STMT = "INSERT INTO REVIEWS VALUES(?,?,?,?,?,?)";
	public final String SELECT_ALL_REVIEWS_STMT = "SELECT * FROM REVIEWS";
	public final String SELECT_REVIEW_BY_BOOK_ID_STMT = "SELECT * FROM REVIEWS"
			+ "WHERE IdBook=?";
	
	
	public final String CREATE_BOOKSTOUSERS_TABLE = "CREATE TABLE BOOKSTOUSERS(IdBook INT,"
			+ "Username varchar(10),"
			+ "Review varchar(50),"
			+ "PRIMARY KEY (IdBook))";
	
	public final String INSERT_BOOKSTOUSER_STMT = "INSERT INTO BOOKSTOUSERS VALUES(?,?,?)";
	public final String SELECT_ALL_BOOKSTOUSERS_STMT = "SELECT * FROM BOOKSTOUSERS";
	public final String SELECT_BOOKSTOUSER_BY_NAME_STMT = "SELECT * FROM BOOKSTOUSERS"
			+ "WHERE Name=?";
	
	public final String CREATE_PURCHASES_TABLE = "CREATE TABLE PURCHASES(IdPurchased INT,"
			+ "IdBook INT,"
			+ "Username varchar(10),"
			+ "DateBought BIGINT,"
			+ "PRIMARY KEY(IdPurchased, IdBook, Username))";
	
	public final String INSERT_PURCHASE_STMT = "INSERT INTO PURCHASES VALUES(?,?,?,?)";
	public final String SELECT_ALL_PURCHASES_STMT = "SELECT * FROM PURCHASES";
	public final String SELECT_PURCHASE_BY_USER_STMT = "SELECT * FROM PURCHASES"
			+ "WHERE username=?";
	
}









































