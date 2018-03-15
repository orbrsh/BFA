package booksForAll.model;


public class Like {
	private int IdBook;
	private String Username;
	private boolean isActive;
	private long dateSetTimeStamp;
	
	public Like(int idBook, String username, boolean isActive, long dateSetTimeStamp) {
		super();
		IdBook = idBook;
		Username = username;
		this.isActive = isActive;
		this.dateSetTimeStamp = dateSetTimeStamp;
	}
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
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public long getDateSetTimeStamp() {
		return dateSetTimeStamp;
	}
	public void setDateSetTimeStamp(long dateSetTimeStamp) {
		this.dateSetTimeStamp = dateSetTimeStamp;
	}
}