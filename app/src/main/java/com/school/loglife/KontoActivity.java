package com.school.loglife;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.school.loglife.Users.User;
import com.school.loglife.Users.UserManager;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KontoActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener{
    private EditText username;
    private EditText password;
    private Button cuser;
    private Button cpass;
    private UserManager manager;
    private TextView utext;
    private TextView ptext;
    private User user;
    private Button leave;
    private static final String AES_KEY = "ASDFGHJKLASDFGHJ";
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        setContentView(R.layout.activity_konto);
        Intent intent = getIntent();
        View mainframe = this.findViewById(android.R.id.content);
        gestureDetector = new GestureDetector(this, this);
        mainframe.setOnTouchListener(this);
        int userid = intent.getIntExtra("userid", -1);
        manager = new UserManager(getApplicationContext());
        username = (EditText) findViewById(R.id.inputname);
        password = (EditText) findViewById(R.id.inputpass);
        leave = (Button) findViewById(R.id.leave);
        utext = (TextView) findViewById(R.id.error);
        cuser = (Button) findViewById(R.id.updatename);
        cpass = (Button) findViewById(R.id.updatepass);
        ptext = (TextView) findViewById(R.id.error1);

        user = manager.getUser(userid);
        if (user != null) {
            username.setText(user.getUsername());
            try {
                password.setText(decrypt(user.getPassword()));
            } catch (Exception e) {
                System.out.println("wrong" + e.getMessage());
            }
        }
        cuser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeName();
            }
        });
        cpass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changePassword();
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cuser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_BUTTON_PRESS:
                        changeName();
                        return true;
                }
                return false;
            }
        });
        cpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_BUTTON_PRESS:
                        changePassword();
                        return true;
                }
                return false;
            }
        });
        leave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_BUTTON_PRESS:
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    public void changePassword() {
        String pass = password.getText().toString();
        if ((pass == null) || (pass.equals(""))) {
            ptext.setTextColor(Color.RED);
            ptext.setText("password cant be empty, please retry");
        } else {
            try {
                user.setPassword(encrypt(pass));
                manager.updateUser(user);
                //manager.deleteAll();
            } catch (Exception e) {
                System.out.println("wrong" + e.getMessage());
            }
            ptext.setTextColor(Color.GREEN);
            ptext.setText("change sucessful!");
        }
    }


    public void changeName() {
        String name = username.getText().toString();
        if ((name == null) || (name.equals(""))) {
            utext.setTextColor(Color.RED);
            utext.setText("username cant be empty, please retry");
        } else {
            User user1 = manager.getUser(name);
            if (user1 != null) {
                utext.setTextColor(Color.RED);
                utext.setText("username already exits");
            } else {
                try {
                    user.setUsername(name);
                    manager.updateUser(user);
                    //manager.deleteAll();
                } catch (Exception e) {
                    System.out.println("wrong" + e.getMessage());
                }
                utext.setTextColor(Color.GREEN);
                utext.setText("change sucessful!");
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

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        finish();
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }
}