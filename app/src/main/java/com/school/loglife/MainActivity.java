package com.school.loglife;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.school.loglife.Users.User;
import com.school.loglife.Users.UserManager;


public class MainActivity extends AppCompatActivity {
    private UserManager manager;
    private EditText u;
    private EditText p;
    private TextView t;
    private Button register;
    private Button login;
    private String username;
    private String password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        manager = new UserManager(getApplicationContext());
        setContentView(R.layout.activity_main);
        u = (EditText) findViewById(R.id.username);
        p = (EditText) findViewById(R.id.password);
        t = (TextView) findViewById(R.id.error);
        register = (Button) findViewById(R.id.Register);
        login = (Button) findViewById(R.id.Login);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initRegister();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initLogin();
            }
        });
    }

    public void initLogin() {
        t.setTextColor(Color.RED);
        username = u.getText().toString();
        password = p.getText().toString();
        if ((username == null) || (username.equals("")) || (password == null) || (password.equals(""))) {
            t.setText("username and password cant be empty, please retry");
            u.setText("");
            p.setText("");
        } else {
            User user = manager.getUser(username, password);
            t.setText("");
            if (user == null) {
                t.setText("username or password invalid, please retry");
                u.setText("");
                p.setText("");
            } else {
                Intent intent = new Intent(this, DiaryActivity.class);
                intent.putExtra("user", user.getUsername());
                startActivity(intent);
            }
        }
    }

    public void initRegister() {
        username = u.getText().toString();
        password = p.getText().toString();
        if ((username == null) || (username.equals("")) || (password == null) || (password.equals(""))) {
            t.setTextColor(Color.RED);
            t.setText("username and password cant be empty, please retry");
            u.setText("");
            p.setText("");
        } else {
            t.setTextColor(Color.GREEN);
            t.setText("register sucessful!");
            u.setText("");
            p.setText("");
            manager.addUser(username, password);
        }
    }
}