/**
 * 
 */
package booksForAll.model;

/**
 * @author light
 *
 */
public class BookLocation {
	
	private int idbook;
	private String username;
	private long bookLocation;
	
	/**
	 * @return the idbook
	 */
	public int getIdbook() {
		return idbook;
	}
	/**
	 * @param idbook the idbook to set
	 */
	public void setIdbook(int idbook) {
		this.idbook = idbook;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the bookLocation
	 */
	public long getBookLocation() {
		return bookLocation;
	}
	/**
	 * @param bookLocation the bookLocation to set
	 */
	public void setBookLocation(long bookLocation) {
		this.bookLocation = bookLocation;
	}
	
	/**
	 * ctor
	 * @param idbook book id
	 * @param username user name
	 * @param bookLocation location on page
	 */
	public BookLocation(int idbook, String username, long bookLocation) {
		super();
		this.idbook = idbook;
		this.username = username;
		this.bookLocation = bookLocation;
	}
	

}
