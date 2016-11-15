package com.example.team5.jianzhili;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yinsong Xu on 5/5/2016.
 */


public class BookPageActivity extends AppCompatActivity {

    private GoogleApiClient client;
    //private static Button button5;



        @Override
        protected void onCreate (Bundle savedInstanceState){
            BookList booklist;
            super.onCreate(savedInstanceState);
            setContentView(R.layout.buy_page);

           // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
           // setSupportActionBar(toolbar);
            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        /*
            button5 = (Button) findViewById(R.id.button5);
            button5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    Intent intent = new Intent(getApplicationContext(), SearchPage.class);
                    startActivity(intent);

                }
            });
        */
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        final static int ADD_ITEM_INTENT = 1; // use to signify result of adding item

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            switch (item.getItemId()) {
            /*
            case R.id.add:
                Intent addItemIntent = new Intent(this, AddItemActivity.class);
                startActivityForResult(addItemIntent, ADD_ITEM_INTENT);
                return (true);
            */
                // BTW, you could handle other menu items here, if your menu had them
            }
            return (super.onOptionsItemSelected(item));
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent returnIntent){

            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case ADD_ITEM_INTENT:
                        String title = returnIntent.getStringExtra("title");
                        String seller = returnIntent.getStringExtra("seller");
                        String price = returnIntent.getStringExtra("price");
                        if (title != null && seller != null) {
                            Toast.makeText(this, title + " (" + seller + ")", Toast.LENGTH_SHORT).show();
                            BookList bookList = (BookList)
                                    getSupportFragmentManager().findFragmentById(R.id.booklist_fragment);
                            //bookList.addBook(new BookInfo(title,seller,price,"test",00.00,"test"));
                        }
                        break;
                    // could handle other intent callbacks here, too
                }
            }

        }
    }

