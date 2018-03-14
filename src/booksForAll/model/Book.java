package booksForAll.model;

public class Book {
	public Book(int idBook, String name, String author, String photo, Float price, String description, String fullHtml) {
		super();
		this.IdBook = idBook;
		Name = name;
		Author = author;
		this.Photo = photo;
		this.Price = price;
		this.Description = description;
		FullHtml = fullHtml;
	}
	
	public long getIdBook() {
		return IdBook;
	}
	public String getName() {
		return Name;
	}
	public String getAuthor() {
		return Author;
	}
	public String getPhoto() {
		return Photo;
	}
	public Float getPrice() {
		return Price;
	}
	public String getDescription() {
		return Description;
	}
	public String getFullHtml() {
		return FullHtml;
	}
	
	private long IdBook;
	private String Name;
	private String Author;
	private String Photo;
	private Float Price;
	private String Description;
	private String FullHtml;
	
}
