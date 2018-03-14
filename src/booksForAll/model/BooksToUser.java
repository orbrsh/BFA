package booksForAll.model;

public class BooksToUser {
	public int getIdBook() {
		return IdBook;
	}
	public void setIdBook(int idBook) {
		IdBook = idBook;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getReview() {
		return Review;
	}
	public void setReview(String review) {
		Review = review;
	}
	public BooksToUser(int idBook, String username, String review) {
		super();
		IdBook = idBook;
		Username = username;
		Review = review;
	}
	private int IdBook;
	private String Username;
	private String Review;
}
