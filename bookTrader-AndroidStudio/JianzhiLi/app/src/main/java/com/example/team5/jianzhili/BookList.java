package com.example.team5.jianzhili;

/**
 * Created by Yinsong Xu on 5/5/2016.
 */

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.database.Cursor;
import android.widget.TextView;

import com.example.team5.jianzhili.http.HttpGet;
import com.example.team5.jianzhili.http.HttpPost;

import org.json.JSONArray;
import org.json.JSONObject;

public class BookList extends ListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        List<BookInfo> books = new ArrayList<BookInfo>();
            //HttpGet post = new HttpGet("http://10.0.3.2:8888/tutorialauth?op=showAllBooks", "UTF-8");
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                try {
                    String url = "http://10.0.2.2:8888/tutorialauth?op=showAllBooks";
                    return new HttpGet(url, "UTF-8").finish();
                    //This JSON string is returned to onPostExecute then passed to populateList
                } catch (final Exception e) {
                    // toastError(e);
                    e.printStackTrace();
                    return "";
                }
            }

            protected void onPostExecute(String result) {
                if (result != null && result.length() > 0 && result.startsWith("[")) {
                    populateList(result);
                } else {
                    errorlist();
                }
            }


        }.execute();

        //ListView lv = getListView();
/*
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),oneBookPage.class);
                String bookid = (String)parent.getItemAtPosition(R.id.bookRow);
                intent.putExtra("bookID",bookid);
                startActivity(intent);
            }
        });
  */
        return super.onCreateView(inflater, container, savedInstanceState);
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
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("bookName", book.getTitle());
            hm.put("author", book.getAuthor());
            hm.put("price",book.getPrice());
            //hm.put("bookid",book.getbookid());
            rows.add(hm);
        }
        String[] from = {"bookName", "author", "price"};
        int[] to = {R.id.bookName, R.id.author, R.id.price};

        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), rows, R.layout.book, from, to);
        setListAdapter(adapter);
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(),oneBookPage.class);
                /*
                final TextView idPosition = (TextView) view.findViewById(R.id.bookRow);
                String bookid = idPosition.getText().toString();
                intent.putExtra("bookID",bookid);
                startActivity(intent);
                */
                final TextView namePosition = (TextView) view.findViewById(R.id.bookName);
                final TextView authorPosition = (TextView) view.findViewById(R.id.author);
                String bookName = namePosition.getText().toString();
                intent.putExtra("bookName",bookName);
                String author = authorPosition.getText().toString();
                intent.putExtra("author",author);

                startActivity(intent);
            }
        });
    }

    public void errorlist() {
        List<BookInfo> books = new ArrayList<BookInfo>();
        BookInfo error = new BookInfo("error","error","error","error","error","error","error","error");
        books.add(error);
        setupList(books);
    }


}
