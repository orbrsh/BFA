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
	public final String CREATE_CUSTOMERS_TABLE = "CREATE TABLE CUSTOMER (Username varchar(10),"
			+ "Password varchar(8),"
			+ "Address varchar(50),"
			+ "Nickname varchar(20),"
			+ "Description varchar(50),"
			+ "Photo varchar(600),"
			+ "Email varchar(100),"
			+ "Phone varchar(10),"
			+ "NOT NULL (Username,Password,Address,Nickname,Description,Photo,Email,Phone)";
		//	+ "PRIMARY KEY (Username),"
		//	+ "UNIQUE (Email,Nickname))";
	public final String INSERT_CUSTOMER_STMT = "INSERT INTO CUSTOMER VALUES(?,?,?,?,?,?,?,?)";
	public final String SELECT_ALL_CUSTOMERS_STMT = "SELECT * FROM CUSTOMER";
	public final String SELECT_CUSTOMER_BY_NAME_STMT = "SELECT * FROM CUSTOMER "
			+ "WHERE Username=?";
	public final String DELETE_CUSTOMER_STMT = "DELETE FROM CUSTOMER WHERE USERNAME=?";
	public final String SELECT_CUSTOMER_BY_NAME_PASS_STMT = "SELECT * FROM CUSTOMER "
			+ "WHERE Username=? AND PASSWORD=?";
	
	
	
	
	
	public final String CREATE_BOOKS_TABLE = "CREATE TABLE BOOKS (IdBooks INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "BookName varchar(100) NOT NULL"
			+ "Author varchar(100) NOT NULL,"
			+ "Photo varchar(100) NOT NULL,"
			+ "Price REAL NOT NULL,"
			+ "Description varchar(600) NOT NULL,"
			+ "FullHtml varchar(300) NOT NULL,";
		//	+ "PRIMARY KEY (IdBooks))";
	public final String INSERT_BOOK_STMT = "INSERT INTO BOOKS (?,?,?,?,?,?)";
	public final String SELECT_ALL_BOOKS_STMT = "SELECT * FROM BOOKS";
	public final String SELECT_BOOK_BY_USERNAME_STMT = "SELECT * FROM BOOKS "
			+ "WHERE username=?";
	public final String SELECT_BOOK_BY_BOOK_ID_STMT = "SELECT * FROM BOOKS "
			+ "WHERE bookid=?";
	public final String DELETE_BOOK_BY_CUSTOMER_STMT = "DELETE FROM BOOKS WHERE Name=?";
	public final String SELECT_BOOK_BY_AUTHOR_STMT = "SELECT * FROM BOOKS "
			+ "WHERE Author=?";
	
	
	
	
	
	public final String CREATE_LIKES_TABLE = "CREATE TABLE LIKES (IdBook INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "BookName varchar(100) NOT NULL,"
			+ "Username varchar(10) NOT NULL,"
			+ "isActive Boolean NOT NULL,"
			+ "dateSetTimeStamp BIGINT, NOT NULL";
		//	+ "PRIMARY KEY (IdBook, Username))";
	public final String INSERT_LIKE_STMT = "INSERT INTO LIKES (?,?,?,?)";
	public final String SELECT_ALL_LIKES_STMT = "SELECT * FROM LIKES";
	public final String SELECT_LIKE_BY_BOOK_ID_STMT = "SELECT * FROM LIKES"
			+ "WHERE IdBook=?";
	public final String SELECT_LIKES_BY_USER_STMT = "SELECT * FROM LIKES "
			+ "WHERE Username=?";
	public final String DELETE_LIKE_BY_CUSTOMER_STMT = "DELETE FROM LIKES WHERE Username=?";
	public final String DELETE_LIKE_BY_NAME_STMT = "DELETE FROM LIKES WHERE Name=?";
	
	public final String SELECT_LIKES_BY_CUSTOMER_AND_BOOK_NAME_STMT = "SELECT * FROM LIKES "
			+ "WHERE Username=? AND Name=?";
	public final String DELETE_LIKE_BY_CUSTOMER_AND_BOOK_NAME_STMT = "DELETE FROM LIKES WHERE Username=? AND Name=?";
	
	
	
	
	
	public final String CREATE_REVIEWS_TABLE = "CREATE TABLE REVIEWS (IdBook INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "BookName varchar(100) NOT NUL,"
			+ "Username varchar(10) NOT NULL,"
			+ "dateWritten BIGINT, NOT NULL" //TODO: do we need it ?
			+ "dateApproved BIGINT, NOT NULL"
			+ "isApproved INT, NOT NULL"
			+ "reviewText varchar(600), NOT NULL";
		//	+ "PRIMARY KEY(IdBook, Username))";
	public final String INSERT_REVIEW_STMT = "INSERT INTO REVIEWS (?,?,?,?,?,?)";
	public final String SELECT_ALL_REVIEWS_STMT = "SELECT * FROM REVIEWS";
	public final String SELECT_REVIEW_BY_BOOK_ID_STMT = "SELECT * FROM REVIEWS"
			+ "WHERE IdBook=?";
	//public final String SELECT_REVIEWS_BY_COSTUMER_STMT = "SELECT FROM REVIEWS WHERE Username=?";	
	public final String SELECT_ALL_APPROVED_REVIEWS_STMT = "SELECT * FROM REVIEWS WHERE isApproved=1";
	public final String SELECT_ALL_UNAPPROVED_REVIEWS_STMT = "SELECT * FROM REVIEWS WHERE isApproved=0";
	public final String SELECT_REVIEWS_BY_BOOK_NAME_STMT = "SELECT * FROM REVIEWS "
			+ "WHERE (BookName=? AND isApproved=1)";
	
	public final String SELECT_REVIEWS_BY_CUSTOMER_STMT = "SELECT * FROM REVIEWS "
			+ "WHERE (Username=? AND isApproved=1)";
	
	public final String DELETE_REVIEWS_BY_USER_STMT = "DELETE FROM REVIEWS WHERE Username=?";
	
	public final String DELETE_REVIEWS_BY_ID_STMT = "DELETE FROM REVIEWS WHERE IdBook=?";
	public final String UPDATE_REVIEWS_APPROVE = "UPDATE REVIEWS SET isApproved=1 WHERE IdBook=?";
	
	
	
	
	
	
	public final String CREATE_BOOKSTOUSERS_TABLE = "CREATE TABLE BOOKSTOUSERS(IdBook INT,"
			+ "Username varchar(10),"
			+ "Review varchar(50),"
			+ "PRIMARY KEY (IdBook))";
	
	public final String INSERT_BOOKSTOUSER_STMT = "INSERT INTO BOOKSTOUSERS VALUES(?,?,?)";
	public final String SELECT_ALL_BOOKSTOUSERS_STMT = "SELECT * FROM BOOKSTOUSERS";
	public final String SELECT_BOOKSTOUSER_BY_NAME_STMT = "SELECT * FROM BOOKSTOUSERS"
			+ "WHERE Name=?";
	
	
	
	
	public final String CREATE_PURCHASES_TABLE = "CREATE TABLE PURCHASES( IdBook INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "IdPurchased INT, NOT NULL"
			+ "BookName varchar(100) NOT NULL,"
			+ "Username varchar(10) NOT NULL,"
			+ "DateBought BIGINT NOT NULL,";
		//	+ "PRIMARY KEY(IdPurchased, BookName, Username))";
	
	public final String INSERT_PURCHASE_STMT = "INSERT INTO PURCHASES VALUES(?,?,?,?)";
	public final String SELECT_ALL_PURCHASES_STMT = "SELECT * FROM PURCHASES";
	public final String SELECT_PURCHASE_BY_CUSTOMER_STMT = "SELECT * FROM PURCHASES"
			+ "WHERE username=?";
	
	
	public final String DELETE_PURCHASES_BY_CUSTOMER_STMT = "DELETE FROM PURCHASES WHERE Username=?";
	
	public final String DELETE_PURCHASES_BOOK_STMT = "DELETE FROM PURCHASES WHERE BookName=?";
		
	public final String SELECT_PURCHASES_BY_BOOK_NAME_STMT = "SELECT * FROM PURCHASES "
			+ "WHERE BookName=?";
	
	public final String SELECT_PURCHASES_BY_USER_AND_BOOK_NAME_STMT = "SELECT * FROM PURCHASES "
			+ "WHERE Username=? AND BookName=?";
	
	public final String SELECT_PURCHASES_BY_DATE_STMT = "SELECT * FROM PURCHASES "
			+ "WHERE DateBought>=? AND DateBought<=?";
}









































