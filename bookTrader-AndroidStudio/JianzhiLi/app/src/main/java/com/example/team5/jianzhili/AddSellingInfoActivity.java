package com.example.team5.jianzhili;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Yinsong Xu on 5/8/2016.
 */
public class AddSellingInfoActivity extends AppCompatActivity implements BufferThread.StatusListener {


    String username;
    String password;
    long sessionId;

    private BufferThread buffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        sessionId = intent.getLongExtra("session", 0L);

        if (username == null || password == null || sessionId <= 0L) {
            UiUtil.toastOnUiThread(this, "Error: username, password, session");
            return;
        }

        setContentView(R.layout.adding_selling_info_page);

        buffer = new BufferThread(this, new FileManager(this), new Server(this, username, password, sessionId));
        buffer.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (buffer != null) {
            if (!buffer.isInterrupted())
                buffer.interrupt();
            buffer.cleanup();
        }
    }

    @Override
    public void report(String msg) {
        UiUtil.toastOnUiThread(this, msg);
    }

    public void doSave(View v) {
        try {
            BookInfo entry = new BookInfo();
            entry.setTitle(UiUtil.readText(this, R.id.sellingBookName));
            if (entry.getTitle().length() == 0)
                throw new IllegalArgumentException("Please enter a title.");

            entry.setAuthor(UiUtil.readText(this, R.id.sellingBookAuthor));
            if (entry.getAuthor().length() == 0)
                throw new IllegalArgumentException("Please enter an author.");

            entry.setEdition(UiUtil.readText(this, R.id.sellingBookEdition));
            if (entry.getEdition().length() == 0)
                throw new IllegalArgumentException("Please enter an edition.");

            entry.setContactInfo(UiUtil.readText(this, R.id.sellingContactInfo));
            if (entry.getContactInfo().length() == 0)
                throw new IllegalArgumentException("Please enter contact information.");

            entry.setCollegeName(UiUtil.readText(this, R.id.sellingCollege));
            if (entry.getCollegeName().length() == 0)
                throw new IllegalArgumentException("Please enter the college.");

            entry.setClassName(UiUtil.readText(this, R.id.sellingClass));
            if (entry.getClassName().length() == 0)
                throw new IllegalArgumentException("Please enter the college.");

            entry.setPrice(UiUtil.readText(this, R.id.sellingPrice));
            if (entry.getPrice().length() == 0)
                throw new IllegalArgumentException("Please enter the price.");


            BufferThread tmp = buffer;
            if (tmp != null) {
                tmp.write(entry);

                // reset the screen
                UiUtil.writeText(this, R.id.sellingBookName, "");
                UiUtil.writeText(this, R.id.sellingBookAuthor, "");
                UiUtil.writeText(this, R.id.sellingBookEdition, "");
                UiUtil.writeText(this, R.id.sellingContactInfo, "");
                UiUtil.writeText(this, R.id.sellingCollege, "");
                UiUtil.writeText(this, R.id.sellingClass, "");
                UiUtil.writeText(this, R.id.sellingPrice, "");

                // Return to homepage
                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("session", sessionId);
                startActivity(intent);
                Toast.makeText(AddSellingInfoActivity.this,"Book Added!",Toast.LENGTH_LONG).show();

            } else
                report("Unable to save your work, right now. Sorry!");
        } catch (IllegalArgumentException ex) {
            report(ex.getMessage());
        }
    }


}





