package booksForAll.filters;

//This class is used to read data from queries.
public class Data {
	public String Username;
	public String Password;
	public String BookName;
	public String Description;
	public String IdBook;
	public String Email;
	public String Address;
	public String Phone;
	public String Nickname;
	public String Photo;

	public Data(String username, String password, String bookname, String description, 
			String id, String email, String address, String phone, String nickname, String photo, String balance) {
		super();
		Username = username;
		Password = password;
		BookName = bookname;
		Description = description;
		IdBook = id;
		Email = email;
		Address = address;
		Phone = phone;
		Nickname = nickname;
		Photo = photo;
	}
}
