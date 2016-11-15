package com.example.team5.jianzhili;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avant on 5/9/2016.
 */
public class Databasehelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BookTrader.db";
    private static final String TABLE_NAME = "BOOK";
    private static final String COLUMN_TITLE= "Title";
    private static final String COLUMN_AUTHOR = "Author";
    private static final String COLUMN_EDITION = "Edition";
    private static final String COLUMN_CONTACT = "Contact";
    private static final String COLUMN_COLLEGE = "College";
    private static final String COLUMN_ClASSNAME = "ClassName";
    private static final String COLUMN_PRICE = "Price";


    public Databasehelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table BOOK(ID INTEGER PRIMARY KEY AUTOINCREMENT, Title text, Author text, Edition text, Contact text, College text, ClassName text, Price text)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String Title, String Author, String Edition, String Contact, String College, String ClassName, String Price){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table IF NOT EXISTS BOOK(ID INTEGER PRIMARY KEY AUTOINCREMENT, Title text, Author text, Edition text, Contact text, College text, ClassName text, Price text)");
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, Title);
        contentValues.put(COLUMN_AUTHOR, Author);
        contentValues.put(COLUMN_EDITION, Edition);
        contentValues.put(COLUMN_CONTACT, Contact);
        contentValues.put(COLUMN_COLLEGE, College);
        contentValues.put(COLUMN_ClASSNAME, ClassName);
        contentValues.put(COLUMN_PRICE, Price);

        long result = db.insert("BOOK", null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getDatabaseCursor() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] array = {
                this.COLUMN_TITLE,
                this.COLUMN_AUTHOR,
                this.COLUMN_EDITION,
                this.COLUMN_CONTACT,
                this.COLUMN_COLLEGE,
                this.COLUMN_ClASSNAME,
                this.COLUMN_PRICE
        };

        return db.query(
                this.TABLE_NAME,                      // The table to query
                array,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                this.COLUMN_TITLE                                // The sort order
        );
    }

    public List<BookInfo> getData(){
        String title;
        String author;
        String edition;
        String contact;
        String college;
        String className;
        String price;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from BOOK", null);
        c.moveToFirst();
        List<BookInfo> books = new ArrayList<>();
        while (c.moveToNext()) {
            title = c.getString(c.getColumnIndexOrThrow("Title"));
            author = c.getString(c.getColumnIndexOrThrow("Author"));
            edition = c.getString(c.getColumnIndexOrThrow("Edition"));
            contact = c.getString(c.getColumnIndexOrThrow("Contact"));
            college = c.getString(c.getColumnIndexOrThrow("College"));
            className = c.getString(c.getColumnIndexOrThrow("ClassName"));
            price = c.getString(c.getColumnIndexOrThrow("Price"));
            books.add(new BookInfo(title, "seller",className,college,price,contact,author,edition));
        }
        c.close();
        return books;
    }
}
