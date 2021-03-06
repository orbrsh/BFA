package booksForAll.model;


public class Purchase {
	public int getIdPurchased() {
		return IdPurchased;
	}
	public void setIdPurchased(int idPurchased) {
		IdPurchased = idPurchased;
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
	public long getDateBought() {
		return DateBought;
	}
	public void setDateBought(long Databought) {
		this.DateBought = Databought;
	}
	
	// From DB
	public Purchase(int idPurchased, int idBook, String username, long datebought) {
		super();
		IdPurchased = idPurchased;
		IdBook = idBook;
		Username = username;
		this.DateBought = datebought;
	}
	
	// From JSON
	public Purchase(int idBook, String username, long dateBought) {
		super();
		IdBook = idBook;
		Username = username;
		DateBought = dateBought;
	}


	private int IdPurchased;
	private int IdBook;
	private String Username;
	private long DateBought;
}
