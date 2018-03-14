package booksForAll.model;

import java.sql.Timestamp;

public class Purchase {
	public int getIdParchased() {
		return IdParchased;
	}
	public void setIdParchased(int idParchased) {
		IdParchased = idParchased;
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
	public Timestamp getDataBought() {
		return DataBought;
	}
	public void setDataBought(Timestamp DataBought) {
		this.DataBought = DataBought;
	}
	public Purchase(int idParchased, int idBook, String username, Timestamp DataBought) {
		super();
		IdParchased = idParchased;
		IdBook = idBook;
		Username = username;
		this.DataBought = DataBought;
	}
	private int IdParchased;
	private int IdBook;
	private String Username;
	private Timestamp DataBought;
}
