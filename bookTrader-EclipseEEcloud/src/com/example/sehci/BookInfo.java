package com.example.sehci;

//import java.util.HashSet;
import java.util.List;
//import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.mortbay.log.Log;

//import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class BookInfo {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private String title;

	@Persistent
	private String owner;
	
	@Persistent
	private String className;
	
	@Persistent
	private String collegeName;
	
	@Persistent
	private String price;
	
	@Persistent
	private String contactInfo;
	
	@Persistent
	private String author;
	
	@Persistent
	private String edition;

	@Persistent
	private Long when;



	public long getId() {
		return id != null ? id.longValue() : 0L;
	}

	public String getTitle() {
		return title != null ? title : "";
	}

	public String getOwner() {
		return owner != null ? owner : "";
	}


	public String getClassName() {
		return className != null ? className : "";
	}
	
	public String getCollegeName() {
		return collegeName != null ? collegeName : "";
	}
	
	public String getPrice() {
		return price != null ? price : "";
	}
	
	public String getContactInfo() {
		return contactInfo != null ? contactInfo : "";
	}
	
	public String getAuthor() {
		return author != null ? author : "";
	}
	
	public String getEdition() {
		return edition != null ? edition : "";
	}


	public long getWhen() {
		return when != null ? when.longValue() : 0L;
	}

	public void setTitle(String title) {
		this.title = title != null ? title : "";
	}

	public void setOwner(String owner) {
		this.owner = owner != null ? owner : "";
	}

	public void setClassName(String className) {
		this.className = className != null ? className : "";
	}
	
	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName != null ? collegeName : "";
	}
	
	public void setPrice(String price) {
		this.price = price != null ? price : "";
	}
	
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo != null ? contactInfo : "";
	}
	
	public void setAuthor(String author) {
		this.author = author != null ? author : "";
	}
	
	public void setEdition(String edition) {
		this.edition = edition != null ? edition : "";
	}


	public void setWhen(long when) {
		this.when = when;
	}

	public static BookInfo loadById(long id, PersistenceManager pm) {
		return pm.getObjectById(BookInfo.class, id);
	}

	public static void delete(BookInfo entry, PersistenceManager pm) {
		pm.deletePersistent(entry);
	}

	public static List<BookInfo> listByOwner(String username, PersistenceManager pm) {
		Query query = pm.newQuery(BookInfo.class, "owner == :oo");
		@SuppressWarnings("unchecked")
		List<BookInfo> rv = (List<BookInfo>) query.execute(username);
		rv.size();
		query.closeAll();
		return rv;
	}

	public static List<BookInfo> listByQuery(String username, PersistenceManager pm) {
		//Note that the username  is sent to the query using query.execute(username) 
		//which puts the username into :oo for pm.newQuery(BookInfo.class, "owner == :oo")
		Query query = pm.newQuery(BookInfo.class, "owner == :oo");
		@SuppressWarnings("unchecked")
		List<BookInfo> rv = (List<BookInfo>) query.execute(username);
		rv.size();
		query.closeAll();
		return rv;
	}
	
	public static List<BookInfo> listOfAll(PersistenceManager pm){
		//Queries all entries of our BookInfo table on the datastore
		Query query = pm.newQuery(BookInfo.class);
		query.setOrdering("id");
		
		@SuppressWarnings("unchecked")
		List<BookInfo> rv = (List<BookInfo>) query.execute();
		rv.size(); // forces all records to load into memory
		return rv;
	}
	
	public static List<BookInfo> bookByID(String bookid, PersistenceManager pm){
		Query query = pm.newQuery(BookInfo.class,bookid);
		
		@SuppressWarnings("unchecked")
		List<BookInfo> rv = (List<BookInfo>) query.execute();
		rv.size(); // forces all records to load into memory
		return rv;
	}
	
	public static List<BookInfo> bookByNameAuthor(String title, String author, PersistenceManager pm){
		//Queries entries with title and collegeName arguments
		Query query = pm.newQuery(BookInfo.class, "title == :tt && author == :cn");
		query.setOrdering("id");
		
		@SuppressWarnings("unchecked")
		List<BookInfo> rv = (List<BookInfo>) query.execute(title, author);
		rv.size(); // forces all records to load into memory
		return rv;
	}
	
	public static List<BookInfo> listOfSearch(String title, String collegeName, PersistenceManager pm){
		//Queries entries with title and collegeName arguments
		Query query = pm.newQuery(BookInfo.class, "title == :tt && collegeName == :cn");
		query.setOrdering("id");
		
		@SuppressWarnings("unchecked")
		List<BookInfo> rv = (List<BookInfo>) query.execute(title, collegeName);
		rv.size(); // forces all records to load into memory
		return rv;
	}
}
