package com.example.team5.jianzhili;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;


//Create by Jianzhi Li 06/02/2016

public class MyProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }
    public void hitServer(View v) {
        try {
            final String username = UiUtil.readText(this, R.id.txtPersonName);
            if (username.length() == 0)
                throw new IllegalArgumentException("Please enter your name.");
            final String phone_num = UiUtil.readText(this, R.id.txtPhoneNum);
            if (phone_num.length() == 0)
                throw new IllegalArgumentException("Please enter your phone number.");

            final String EmailAddress = UiUtil.readText(this, R.id.txtEmailAddress);
            if (EmailAddress.length() == 0)
                throw new IllegalArgumentException("Please enter your email address.");

            final String SchoolName = UiUtil.readText(this, R.id.txtSchoolName);
            if (SchoolName.length() == 0)
                throw new IllegalArgumentException("Please enter your school name.");


            final boolean sub = (v.getId() == R.id.btnSub);
            new AsyncTask<Void, Void, Long>() {
                protected void onPreExecute() {
                    UiUtil.writeText(MyProfile.this, R.id.txtStatus, "Hold on a sec...");
                    UiUtil.enableView(MyProfile.this, R.id.btnSub, false);

                }

                @Override
                protected Long doInBackground(Void... params) {
                    try {
                        return Server.init(username,phone_num,sub);
                    } catch (Exception e) {
                        UiUtil.toastOnUiThread(MyProfile.this, "Error: " + e.getMessage());
                        return 0L;
                    }
                }

                protected void onPostExecute(Long sessionId) {
                    if (sessionId > 0) {
                        UiUtil.writeText(MyProfile.this, R.id.txtStatus, "");
                        startHomePage(username, phone_num, EmailAddress, SchoolName, sessionId);
                    } else {
                        UiUtil.writeText(MyProfile.this, R.id.txtStatus, "Please try again...");
                    }
                    UiUtil.enableView(MyProfile.this, R.id.btnSub, true);

                }
            }.execute();
        } catch (IllegalArgumentException e) {
            UiUtil.toastOnUiThread(this, e.getMessage());
        }
    }

    private void startHomePage(String username, String phone_num, String EmailAdress, String SchoolName, long sessionId) {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.putExtra("Username", username);
        intent.putExtra("Phone Number", phone_num);
        intent.putExtra("Email Address", EmailAdress);
        intent.putExtra("School Name", SchoolName);
        intent.putExtra("session", sessionId);
        startActivity(intent);
    }


    }


