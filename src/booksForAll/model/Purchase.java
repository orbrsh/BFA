package booksForAll.model;


public class Purchase {
	public int getIdPurchased() {
		return IdPurchased;
	}
	public void setIdPurchased(int idPurchased) {
		IdPurchased = idPurchased;
	}
	public String getBookName() {
		return BookName;
	}
	public void setBookName(String bookname) {
		BookName = bookname;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public long getDateBought() {
		return DateBought;
	}
	public void setDateBought(long Databought) {
		this.DateBought = Databought;
	}
	public Purchase(int idPurchased, String bookName, String username, long datebought) {
		super();
		IdPurchased = idPurchased;
		BookName = bookName;
		Username = username;
		this.DateBought = datebought;
	}
	private int IdPurchased;
	private String BookName;
	private String Username;
	private long DateBought;
}
