package com.example.team5.jianzhili;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void hitServer(View v) {
        try {
            final String username = UiUtil.readText(this, R.id.txtUsername);
            if (username.length() == 0)
                throw new IllegalArgumentException("Please enter a username.");
            final String password = UiUtil.readText(this, R.id.txtPassword);
            if (password.length() == 0)
                throw new IllegalArgumentException("Please enter a password.");

            final boolean reg = (v.getId() == R.id.btnRegister);
            new AsyncTask<Void, Void, Long>() {
                protected void onPreExecute() {
                    UiUtil.writeText(LoginActivity.this, R.id.txtStatus, "Hold on a sec...");
                    UiUtil.enableView(LoginActivity.this, R.id.btnRegister, false);
                    UiUtil.enableView(LoginActivity.this, R.id.btnLogin, false);
                }

                @Override
                protected Long doInBackground(Void... params) {
                    try {
                        return Server.init(username, password, reg);
                    } catch (Exception e) {
                        UiUtil.toastOnUiThread(LoginActivity.this, "Error: " + e.getMessage());
                        return 0L;
                    }
                }

                protected void onPostExecute(Long sessionId) {
                    if (sessionId > 0) {
                        UiUtil.writeText(LoginActivity.this, R.id.txtStatus, "");
                        startHomePage(username, password, sessionId);
                    } else {
                        UiUtil.writeText(LoginActivity.this, R.id.txtStatus, "Please try again...");
                    }
                    UiUtil.enableView(LoginActivity.this, R.id.btnRegister, true);
                    UiUtil.enableView(LoginActivity.this, R.id.btnLogin, true);
                }
            }.execute();
        } catch (IllegalArgumentException e) {
            UiUtil.toastOnUiThread(this, e.getMessage());
        }
    }

    private void startHomePage(String username, String password, long sessionId) {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        intent.putExtra("session", sessionId);
        startActivity(intent);
    }

}
