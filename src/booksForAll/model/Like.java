package booksForAll.model;

import java.sql.Timestamp;

public class Like {
	public long getIdBook() {
		return IdBook;
	}
	public void setIdBook(long idBook) {
		IdBook = idBook;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public Timestamp getDataSet() {
		return dataSet;
	}
	public void setDataSet(Timestamp dataSet) {
		this.dataSet = dataSet;
	}
	public Like(long idBook, String username, boolean isActive, Timestamp dataSet) {
		super();
		IdBook = idBook;
		Username = username;
		this.isActive = isActive;
		this.dataSet = dataSet;
	}
	private long IdBook;
	private String Username;
	private boolean isActive;
	private Timestamp dataSet;
}
