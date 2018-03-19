package booksForAll.model;

import java.sql.Timestamp;

public class Purchase {
	public Timestamp getTimeBought() {
		return TimeBought;
	}
	public void setTimeBought(Timestamp timeBought) {
		TimeBought = timeBought;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
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
	public Purchase(String bookName, String username, Timestamp timeBought, Double price) {
		super();
	//	IdPurchased = idPurchased;
		BookName = bookName;
		Username = username;
		this.TimeBought = timeBought;
		Price = price;
	}
	//private int IdPurchased;
	private String BookName;
	private String Username;
	private Timestamp TimeBought;
	private double Price;
}
