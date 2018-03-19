package booksForAll.model;

import java.sql.Timestamp;

//import java.sql.Timestamp;

public class Review {
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getBookName() {
		return BookName;
	}
	public void setBookName(String bookName) {
		BookName = bookName;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public Timestamp getDateApproved() {
		return dateApproved;
	}
	public void setDateApproved(Timestamp dataApproved) {
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
	public Review(int id, String bookName, String username, Timestamp dateApproved, int isApproved,
			String reviewText) {
		super();
		BookName = bookName;
		Username = username;
	//	this.dateWritten = dateWritten;
		this.dateApproved = dateApproved;
		this.isApproved = isApproved;
		this.reviewText = reviewText;
	}
	private int Id;
	private String BookName;
	private String Username;
	//private long dateWritten;
	private Timestamp dateApproved;
	private int isApproved;
	private String reviewText;
	
}
