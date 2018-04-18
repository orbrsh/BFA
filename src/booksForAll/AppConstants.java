package booksForAll;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;

import booksForAll.model.Customer;
import booksForAll.model.Book;
import booksForAll.model.Like;
import booksForAll.model.Review;
//import booksForAll.model.BooksToUser;
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
//	public final Type BOOKSTOUSER_COLLECTION = new TypeToken<Collection<BooksToUser>>() {}.getType();
	
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
	public final String CREATE_CUSTOMERS_TABLE = "CREATE TABLE CUSTOMER("
			+ "Username varchar(10),"
			+ "Password varchar(8),"
			+ "Address varchar(100),"
			+ "Nickname varchar(20),"
			+ "Description varchar(50),"
			+ "Photo varchar(50),"
			+ "Email varchar(100),"
			+ "Phone varchar(10),"
			+ "isActive INT DEFAULT 1,"
			+ "PRIMARY KEY (Username),"
			+ "UNIQUE (Email,Nickname))";
	public final String INSERT_CUSTOMER_STMT = "INSERT INTO CUSTOMER (Username, Password, Address, Nickname, "
			+ "Description, Photo, Email, Phone) VALUES(?,?,?,?,?,?,?,?)";
	public final String SELECT_ALL_ACTIVE_CUSTOMERS_STMT = "SELECT * FROM CUSTOMER "
			+ "WHERE isActive = 1";
	
	public final String SELECT_CUSTOMER_BY_NAME_STMT = "SELECT * FROM CUSTOMER "
			+ "WHERE Username=?";
	public final String SELECT_CUSTOMER_BY_NAME_EMAIL_NICK_STMT = "SELECT * FROM CUSTOMER "
			+ "WHERE Username=? OR Email=? OR Nickname=?"; // for registration
	public final String REMOVE_CUSTOMER_BY_NAME_STMT = "UPDATE CUSTOMER SET isActive=0 "
			+ "WHERE Username=?";
	
	
	public final String CREATE_BOOKS_TABLE = "CREATE TABLE BOOKS ("
			+ "IdBooks INT,"
			+ "Name varchar(100),"
			+ "Author varchar(100),"
			+ "Photo varchar(100),"
			+ "Price REAL,"
			+ "Description varchar(650),"
			+ "FullHtml varchar(300),"
			+ "PRIMARY KEY (IdBooks))";
	public final String INSERT_BOOK_STMT = "INSERT INTO BOOKS (IdBooks, Name, Author, Photo, Price, Description, FullHtml) "
			+ "VALUES(?,?,?,?,?,?,?)";
	public final String SELECT_ALL_BOOKS_STMT = "SELECT * FROM BOOKS";
	public final String SELECT_BOOK_BY_NAME_STMT = "SELECT * FROM BOOKS "
			+ "WHERE Name=?";
	public final String SELECT_BOOK_BY_BOOKID_STMT = "SELECT * FROM BOOKS "
			+ "WHERE IdBooks=?";
	
	
	public final String CREATE_LIKES_TABLE = "CREATE TABLE LIKES("
			+ "IdBook INT,"
			+ "Username varchar(10),"
			//+ "isActive INT,"
			+ "dateSetTimeStamp BIGINT,"
			+ "PRIMARY KEY (IdBook, Username))";
	
	public final String INSERT_LIKE_STMT = "INSERT INTO LIKES (IdBook, Username, dateSetTimeStamp) VALUES(?,?,?)";
	public final String SELECT_ALL_LIKES_STMT = "SELECT * FROM LIKES"; // TODO: is needed?
	public final String SELECT_LIKE_BY_USERNNAME_STMT = "SELECT * FROM LIKES "
			+ "WHERE Username=?"; // TODO: is needed?
	public final String SELECT_LIKE_BY_USERNNAME_BOOKID_STMT = "SELECT * FROM LIKES "
			+ "WHERE Username=? AND IdBook=?";
	public final String SELECT_ALL_LIKES_BY_BOOKID_STMT = "SELECT * FROM LIKES "
			+ "WHERE IdBook=?";
	public final String REMOVE_LIKE_STMT = "DELETE FROM LIKES "
			+ "WHERE Username=? AND IdBook=?";
	
	
	public final String CREATE_REVIEWS_TABLE = "CREATE TABLE REVIEWS("
			+ "IdReviews INT NOT NULL GENERATED ALWAYS " 
			+ "AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "IdBook INT,"
			+ "Username varchar(10),"
			+ "dateWritten BIGINT,"
			+ "dateApproved BIGINT DEFAULT 0,"
			+ "isApproved INT DEFAULT 0,"
			+ "reviewText varchar(650),"
			+ "PRIMARY KEY(IdReviews, IdBook, Username))";
	
	public final String INSERT_REVIEW_STMT = "INSERT INTO REVIEWS (IdBook, Username, dateWritten,"
			+ " dateApproved, isApproved, reviewText) VALUES(?,?,?,?,?,?)";
	public final String INSERT_NEW_REVIEW_STMT = "INSERT INTO REVIEWS (IdBook, Username, dateWritten,"
			+ " reviewText) VALUES(?,?,?,?)";
	public final String SELECT_ALL_REVIEWS_STMT = "SELECT * FROM REVIEWS";
	public final String SELECT_ALL_REVIEWS_BY_BOOK_STMT = "SELECT * FROM REVIEWS "
			+ "WHERE IdBook=?";
	public final String SELECT_ALL_APPROVED_REVIEWS_BY_BOOK_STMT = "SELECT * FROM REVIEWS "
			+ "WHERE IdBook=? AND isApproved=1";
	public final String SELECT_ALL_UN_APPROVED_REVIEWS = "SELECT * FROM REVIEWS "
			+ "WHERE isApproved=0";
	public final String SELECT_ALL_REVIEWS_BY_USER_STMT = "SELECT * FROM REVIEWS "
			+ "WHERE Username=?";
	public final String APPROVE_REVIEW_BY_REVIEWID_STMT = "UPDATE REVIEWS SET isApproved = 1 "
			+ "WHERE IdReviews=?";
	
	
//	// might not be needed 
//	// start
//	public final String CREATE_BOOKSTOUSERS_TABLE = "CREATE TABLE BOOKSTOUSERS("
//			+ "IdBook INT,"
//			+ "Username varchar(10),"
//			+ "Review varchar(50),"
//			+ "PRIMARY KEY (IdBook))";
//	
//	public final String INSERT_BOOKSTOUSER_STMT = "INSERT INTO BOOKSTOUSERS VALUES(?,?,?)";
//	public final String SELECT_ALL_BOOKSTOUSERS_STMT = "SELECT * FROM BOOKSTOUSERS";
//	public final String SELECT_BOOKSTOUSER_BY_NAME_STMT = "SELECT * FROM BOOKSTOUSERS "
//			+ "WHERE Name=?";
//	// end
//	
	
	
	public final String CREATE_PURCHASES_TABLE = "CREATE TABLE PURCHASES("
			+ "IdPurchase INT NOT NULL GENERATED ALWAYS "
			+ "AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "IdBook INT NOT NULL,"
			+ "Username varchar(10) NOT NULL,"
			+ "DateBought BIGINT,"
			+ "location BIGINT DEFAULT 0,"
			+ "PRIMARY KEY(IdPurchase, IdBook, Username))";
	
	public final String INSERT_PURCHASE_STMT = "INSERT INTO PURCHASES (IdBook, Username, DateBought) VALUES(?,?,?)";
	public final String SELECT_ALL_PURCHASES_STMT = "SELECT * FROM PURCHASES";
	public final String SELECT_PURCHASE_BY_USERNAME_STMT = "SELECT * FROM PURCHASES "
			+ "WHERE Username=?";
	public final String SELECT_PURCHASE_BY_BOOKID_STMT = "SELECT * FROM PURCHASES "
			+ "WHERE IdBook=?";
	public final String SELECT_PURCHASE_BY_USERNAME_AND_BOOKID_STMT = "SELECT * FROM PURCHASES "
			+ "WHERE Username=? AND IdBook=?";
	public final String UPDATE_BOOK_LOCATION_STMT = "UPDATE PURCHASES SET location = ? "
			+ "WHERE IdBook=? AND Username=?";
	public final String SELECT_BOOK_LOCATION_STMT = "SELECT IdBook, Username, location FROM PURCHASES "
			+ "WHERE IdBook=? AND Username=?";
	
}









































