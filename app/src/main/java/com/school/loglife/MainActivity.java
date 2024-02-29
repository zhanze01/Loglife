package com.school.loglife;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.school.loglife.Users.User;
import com.school.loglife.Users.UserManager;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class MainActivity extends AppCompatActivity {
    private UserManager manager;
    private EditText u;
    private EditText p;
    private TextView t;
    private Button register;
    private Button login;
    private String username;
    private String password;
    private static final String AES_KEY = "ASDFGHJKLASDFGHJ";

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
        String password1 = "";
        if ((username == null) || (username.equals("")) || (password == null) || (password.equals(""))) {
            t.setText("username and password cant be empty, please retry");
            u.setText("");
            p.setText("");
        } else {
            User user = manager.getUser(username);
            t.setText("");
            try {
                password1 = decrypt(user.getPassword());
                System.out.println(password1);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if ((user == null) || (!password1.equals(password))) {
                t.setText("username or password invalid, please retry");
                u.setText("");
                p.setText("");
            } else {
                Intent intent = new Intent(this, DiaryActivity.class);
                intent.putExtra("userid", user.getUserId());
                startActivity(intent);
                //manager.deleteAll();
            }
        }
    }

    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKey secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    // Decrypt data
    public String decrypt(String encryptedData) throws Exception {
        byte[] encryptedBytes = Base64.decode(encryptedData, Base64.DEFAULT);
        SecretKey secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
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
            User user = manager.getUser(username);
            if (user != null) {
                t.setTextColor(Color.RED);
                t.setText("username already exits");
                u.setText("");
                p.setText("");
            } else {
                try {
                    manager.addUser(username, encrypt(password));
                    System.out.println(encrypt(password));
                } catch (Exception e) {
                    System.out.println("wrong" + e.getMessage());
                }
                t.setTextColor(Color.GREEN);
                t.setText("register sucessful!");
                u.setText("");
                p.setText("");

            }
        }
    }
}