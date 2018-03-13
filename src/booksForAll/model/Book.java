package booksForAll.model;

public class Book {
	public int getIdBooks() {
		return idBooks;
	}

	public String getName() {
		return Name;
	}
	
	public String getAuthor() {
		return Author;
	}
	public void setAuthor(String author) {
		Author = author;
	}
	public String getPhoto() {
		return photo;
	}
	
	public Float getPrice() {
		return price;
	}
	
	public String getDescription() {
		return description;
	}

	public Book(int idBooks, String name, String author, String photo, Float price, String description) {
		super();
		this.idBooks = idBooks;
		Name = name;
		Author = author;
		this.photo = photo;
		this.price = price;
		this.description = description;
	}
	private int idBooks;
	private String Name;
	private String Author;
	private String photo;
	private Float price;
	private String description;
}

