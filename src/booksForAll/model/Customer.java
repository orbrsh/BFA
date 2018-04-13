package booksForAll.model;

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
	public int getPhone() {
		return Phone;
	}
	public void setPhone(int phone) {
		Phone = phone;
	}
	public Customer(String username, String password, String address, String nickname, String description, String photo,
			String mail, int phone) {
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
	
	// ctor from db to client side
	public Customer(String username, String address, String nickname, String description, String photo,
			String mail, int phone) {
		super();
		Username = username;
		Password = "";
		Address = address;
		Nickname = nickname;
		this.Description = description;
		this.Photo = photo;
		Email = mail;
		Phone = phone;
		
	}
	
	private String Username;
	private String Password;
	private String Address;
	private String Nickname;
	private String Description;
	private String Photo;
	private String Email;
	private int Phone;
	
	

	
	
	
}
