package booksForAll.model;


public class Like {
	private String BookName;
	private String Username;
	private boolean isActive;
	private long dateSetTimeStamp;
	
	public Like(String bookName, String username, boolean isActive, long dateSetTimeStamp) {
		super();
		BookName = bookName;
		Username = username;
		this.isActive = isActive;
		this.dateSetTimeStamp = dateSetTimeStamp;
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