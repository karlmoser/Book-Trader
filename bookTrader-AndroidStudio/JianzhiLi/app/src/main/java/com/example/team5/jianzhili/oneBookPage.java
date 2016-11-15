package com.example.team5.jianzhili;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.team5.jianzhili.http.HttpGet;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yinsong Xu on 6/4/2016.
 */
public class oneBookPage extends AppCompatActivity {
    String bookName;
    String author;
    //String bookid;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        bookName = intent.getStringExtra("bookName");
        getIntent().removeExtra("bookName");
        author = intent.getStringExtra("author");
        getIntent().removeExtra("author");
        //bookid = intent.getStringExtra("bookID");
        //getIntent().removeExtra("bookID");
        setContentView(R.layout.one_book_page);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void... params) {
                try{
                    //String url = "http://10.0.2.2:8888/tutorialauth?op=bookByID&id=" + bookid;
                    String url = "http://10.0.2.2:8888/tutorialauth?op=bookByNameAuthor&bookName=" + bookName + "&author=" + author;
                    return new HttpGet(url,"UTF-8").finish();
                } catch (final Exception e){
                    e.printStackTrace();
                    return "";
                }
            }

            protected void onPostExecute(String result){
                if (result != null && result.length() > 0 && result.startsWith("[")) {
                    populateList(result);
                } else {
                    errorlist();
                }
            }
        }.execute();
    }

    private void populateList(String json){
        List<BookInfo> books = new ArrayList<BookInfo>();
        try {
            JSONArray arr = new JSONArray(json);
            if (arr != null){
                int len = arr.length();
                for(int i = 0; i<len; i++){
                    String obj = arr.getJSONObject(i).toString();
                    BookInfo temp = BookInfo.fromJson(obj);
                    books.add(temp);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            errorlist();
        }
        setupList(books);
    }

    private void setupList(List<BookInfo> books) {

        List<HashMap<String, String>> rows = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < books.size(); i++) {
            BookInfo book = books.get(i);
            TextView bookName = (TextView)findViewById(R.id.one_bookname);
            TextView collegeName = (TextView)findViewById(R.id.one_college);
            TextView className = (TextView)findViewById(R.id.one_class);
            TextView contact = (TextView)findViewById(R.id.one_contact);
            TextView edition = (TextView)findViewById(R.id.one_edition);
            TextView author = (TextView)findViewById(R.id.one_author);
            TextView price = (TextView)findViewById(R.id.one_price);
            try{
                bookName.setText(book.getTitle());
                collegeName.setText(book.getCollegeName());
                className.setText(book.getClassName());
                contact.setText(book.getContactInfo());
                edition.setText(book.getEdition());
                author.setText(book.getAuthor());
                price.setText(book.getPrice());
            } catch (NullPointerException e){
                errorlist();
            }
        }
    }
    private void setupList(BookInfo book) {

    }
    public void errorlist() {
        List<BookInfo> books = new ArrayList<BookInfo>();
        BookInfo error = new BookInfo("error","error","error","error","error","error","error","error");
        books.add(error);
        setupList(books);
    }
}
