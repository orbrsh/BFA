package booksForAll.model;

//import java.sql.Timestamp;

public class Review {
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
	public long getDateWritten() {
		return dateWritten;
	}
	public void setDateWritten(long dataWritten) {
		this.dateWritten = dataWritten;
	}
	public long getDateApproved() {
		return dateApproved;
	}
	public void setDateApproved(long dataApproved) {
		this.dateApproved = dataApproved;
	}
	public int getIsApproved() {
		return isApproved;
	}
	public void setIsApproved(int isApproved) {
		this.isApproved = isApproved;
	}
	public String getReviewText() {
		return reviewText;
	}
	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}
	public Review(int idBook, String username, long dateWritten, long dateApproved, int isApproved,
			String reviewText) {
		super();
		IdBook = idBook;
		Username = username;
		this.dateWritten = dateWritten;
		this.dateApproved = dateApproved;
		this.isApproved = isApproved;
		this.reviewText = reviewText;
	}
	private int IdBook;
	private String Username;
	private long dateWritten;
	private long dateApproved;
	private int isApproved;
	private String reviewText;
	
}
