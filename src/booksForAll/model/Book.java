package booksForAll.model;

import java.util.ArrayList;
import java.util.Collection;

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
	
	public Book(int idBook, String name, String author, String photo, Float price, String description) {
		super();
		IdBook = idBook;
		Name = name;
		Author = author;
		Photo = photo;
		Price = price;
		Description = description;
	}


	public int getIdBook() {
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
	
	public Collection<Like> getLikes() {
		if (likes == null)
			likes = new ArrayList<Like>();
		return likes;
	}

	public void setLikes(Collection<Like> likes) {
		this.likes = likes;
	}

	public Collection<Review> getReviews() {
		if (reviews == null)
			reviews = new ArrayList<Review>();
		return reviews;
	}

	public void setReviews(Collection<Review> reviews) {
		this.reviews = reviews;
	}

	private int IdBook;
	private String Name;
	private String Author;
	private String Photo;
	private Float Price;
	private String Description;
	private String FullHtml;
	private Collection<Like> likes;
	private Collection<Review> reviews;
	
}
