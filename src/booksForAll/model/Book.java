package booksForAll.model;
 import java.util.List;
 
public class Book {
	public Book(String name, String author, String photo, double price, String description, String fullHtml,
			int LikesNum, List<Like> likes, List<Review> reviews) {
		//super();
		BookName = name;
		Author = author;
		this.Photo = photo;
		this.Price = price;
		this.Description = description;
		FullHtml = fullHtml;
		this.LikesNum = LikesNum;
		this.Likes = likes;
		this.Reviews = reviews;
	}
	
	public String getName() {
		return BookName;
	}
	public String getAuthor() {
		return Author;
	}
	public String getPhoto() {
		return Photo;
	}
	public double getPrice() {
		return Price;
	}
	public String getDescription() {
		return Description;
	}
	public String getFullHtml() {
		return FullHtml;
	}
	public int getLikesNum() {
		return LikesNum;
	}
	public void setLikesNum(int likesNum) {
		LikesNum = likesNum;
	}
	public List<Like> getLikes() {
		return Likes;
	}
	public void setLikes(List<Like> likeUsernames) {
		this.Likes = likeUsernames;
	}
	public List<Review> getReviews() {
		return Reviews;
	}
	public void setReviews(List<Review> reviews) {
		this.Reviews = reviews;
	}
	
	private String BookName;
	private String Author;
	private String Photo;
	private double Price;
	private String Description;
	private String FullHtml;
	private int LikesNum;
	private List<Like> Likes;
	private List<Review> Reviews;
	
}
