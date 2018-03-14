package booksForAll.model;

import java.sql.Timestamp;

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
	public Timestamp getDataWritten() {
		return dataWritten;
	}
	public void setDataWritten(Timestamp dataWritten) {
		this.dataWritten = dataWritten;
	}
	public Timestamp getDataApproved() {
		return dataApproved;
	}
	public void setDataApproved(Timestamp dataApproved) {
		this.dataApproved = dataApproved;
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
	public Review(int idBook, String username, Timestamp dataWritten, Timestamp dataApproved, int isApproved,
			String reviewText) {
		super();
		IdBook = idBook;
		Username = username;
		this.dataWritten = dataWritten;
		this.dataApproved = dataApproved;
		this.isApproved = isApproved;
		this.reviewText = reviewText;
	}
	private int IdBook;
	private String Username;
	private Timestamp dataWritten;
	private Timestamp dataApproved;
	private int isApproved;
	private String reviewText;
	
}
