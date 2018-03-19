package booksForAll.filters;

//This class is used to read data from queries.
public class Data {
	public String Username;
	public String Password;
	public String BookName;
	public String Author;
	public String Description;
	public String Id;
	public String Email;
	public String Address;
	public String Phone;
	public String Nickname;
	public String Photo;
	public String reviewText;


	public Data(String username, String password, String bookname,String author, String description, 
			String id, String email, String address, String phone, String nickname, String photo, String balance, String text) {
		super();
		Username = username;
		Password = password;
		BookName = bookname;
		Author = author;
		Description = description;
		Id = id;
		Email = email;
		Address = address;
		Phone = phone;
		Nickname = nickname;
		Photo = photo;
		reviewText = text;
	}
}
