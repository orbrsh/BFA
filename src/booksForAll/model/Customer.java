package booksForAll.model;

import java.util.ArrayList;

/**
 * A simple bean to hold data
 */
public class Customer {
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getNickname() {
		return Nickname;
	}
	public void setNickname(String nickname) {
		Nickname = nickname;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		this.Description = description;
	}
	public String getPhoto() {
		return Photo;
	}
	public void setPhoto(String photo) {
		this.Photo = photo;
	}
	public String getMail() {
		return Email;
	}
	public void setMail(String mail) {
		Email = mail;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public ArrayList<Book> getBooks() {
		return books;
	}
	public void setBooks(ArrayList<Book> books) {
		this.books = books;
	}
	public Customer(String username, String password, String address, String nickname, String description, String photo,
			String mail, String phone) {
		super();
		Username = username;
		Password = password;
		Address = address;
		Nickname = nickname;
		this.Description = description;
		this.Photo = photo;
		Email = mail;
		Phone = phone;
		
		
	}
	
	private String Username, Password, Address, Nickname, Description, Photo, Email;//customer "schema"
	private String Phone;
	private ArrayList<Book> books;
	

	
	
	
}
