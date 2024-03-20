package com.school.loglife;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

    private static final String CHANNEL_ID = "channel1";
    private static final String CHANNEL_NAME = "Channel";
    private static final String CHANNEL_DESC = "Channel Notify";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

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

       /* register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initRegister();
            }
        });*/
        register.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initRegister();
                        return true;
                }
                return false;
            }
        });
   /*     login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initLogin();
            }
        });*/

        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initLogin();
                        return true;
                }
                return false;
            }
        });
    }

    public void displayNotificationWithDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                displayNotification();
            }
        }, 10000); // 10 sek
    }

    public void displayNotification() {
        // Benachrichtigung erstellen
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.baseline_deblur_24)
                        .setContentTitle("Mach einen Blog Eintrag")
                        .setContentText("Erstelle jetzt einen neuen Eintrag für deinen Blog")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Erstelle jetzt einen neuen Eintrag für deinen Blog"));

        // Benachrichtigung anzeigen
        NotificationManagerCompat mNotificationMngr = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mNotificationMngr.notify(1, mBuilder.build());
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
                displayNotificationWithDelay();
                startActivity(intent);
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
                    //manager.deleteAll();
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