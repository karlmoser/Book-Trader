package com.example.team5.jianzhili;

import org.json.JSONException;
import org.json.JSONObject;

/*Created by Yinsong Xu*/
public class BookInfo {
    public BookInfo() {
        setWhen(System.currentTimeMillis());
    }

    public BookInfo(String title, String sellerName,String className, String collegeName, String price, String contactInfo, String author, String edition){
        this.title=title;
        this.className=className;
        this.collegeName=collegeName;
        this.sellerName=sellerName;
        this.price=price;
        this.contactInfo=contactInfo;
        this.author=author;
        this.edition=edition;
    }

    private String title;
    private String sellerName;
    private String className;
    private String collegeName;
    private String price;
    private String contactInfo;
    private String author;
    private String edition;
    //private String bookid;
    private Long when;

    public String getTitle() {
        return title != null ? title : "";
    }

   /*
   public String getOwner() {
        return owner != null ? owner : "";
    }
    */


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

    //public String getbookid() {return bookid !=null ? bookid : "";}

    public void setTitle(String title) {
        this.title = title != null ? title : "";
    }

    /*
    public void setOwner(String owner) {
        this.owner = owner != null ? owner : "";
    }
    */

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

    //public void setbookid(String bookid) {this.bookid = bookid;}
    public String toJson() throws JSONException {
        JSONObject tmp = new JSONObject();

        tmp.put("when", getWhen());
        tmp.put("title", getTitle());
        // tmp.put("owner",  .getOwner());
        tmp.put("className", getClassName());
        tmp.put("collegeName", getCollegeName());
        tmp.put("price", getPrice());
        tmp.put("contactInfo", getContactInfo());
        tmp.put("author", getAuthor());
        tmp.put("edition", getEdition());

        return tmp.toString();

    }

    public static BookInfo fromJson(String json) throws JSONException {
        JSONObject tmp = new JSONObject(json);
        BookInfo entry = new BookInfo();
        entry.setWhen(tmp.getLong("when"));
        entry.setTitle(tmp.getString("title"));
        entry.setClassName(tmp.getString("className"));
        entry.setCollegeName(tmp.getString("collegeName"));
        entry.setPrice(tmp.getString("price"));
        entry.setContactInfo(tmp.getString("contactInfo"));
        entry.setAuthor(tmp.getString("author"));
        entry.setEdition(tmp.getString("edition"));
        //entry.setbookid(tmp.getString("id"));
        return entry;

    }

}

