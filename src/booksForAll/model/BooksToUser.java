package booksForAll.model;

public class BooksToUser {
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
	public BooksToUser(String username, String review) {
		super();
		Username = username;
		Review = review;
	}
	private String Username;
	private String Review;
}
