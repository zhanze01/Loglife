package com.school.loglife;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;
import androidx.core.graphics.drawable.IconCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.school.loglife.Diaries.Diary;
import com.school.loglife.Diaries.DiaryManager;
import com.school.loglife.UI.DiaryAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiaryActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener, PopupMenu.OnMenuItemClickListener {

    private ListView diaryListView;
    private DiaryManager diaryManager;
    private List<Diary> diaryList;
    private DiaryAdapter diaryAdapter;
    private ImageView menu;
    private int userid;
    private GestureDetector gestureDetector;
    private float startX;
    private float startY;
    NotificationManagerCompat mNotificationMngr;

    private static final String CHANNEL_ID = "channel1";
    private static final String CHANNEL_NAME = "Channel";
    private static final String CHANNEL_DESC = "Channel Notify";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        displayNotificationWithDelay();
        gestureDetector = new GestureDetector(this, this);
        initView();
        initEintrag();
        initMenu();
    }

    public void initView() {
        setContentView(R.layout.activity_diary);
        diaryListView = findViewById(R.id.diaryList);
        menu = findViewById(R.id.menu);
        diaryManager = new DiaryManager(getApplicationContext());
        Intent intent = getIntent();
        userid = intent.getIntExtra("userid", -1);
        List<Diary> diaries = diaryManager.getAllDiaries(userid);
        if (diaries != null) {
            diaryList = diaryManager.getAllDiaries(userid);
        } else {
            diaryList = new ArrayList<>();
        }
        diaryAdapter = new DiaryAdapter(this, android.R.layout.simple_list_item_1, diaryList);
        diaryListView.setAdapter(diaryAdapter);

        diaryListView.setOnTouchListener(this);
        /*diaryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Diary diary = diaryList.get(position);
                deleteDiaryEntry(diary);
                diaryManager.deleteDiary(diary);
                Toast.makeText(DiaryActivity.this, "Entry deleted" + diary.getName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        diaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Diary diary = diaryList.get(position);
                Intent intent = new Intent(getApplicationContext(), DiaryContent.class);
                intent.putExtra("diaryid", diary.getId());
                startActivity(intent);
            }
        });
        */


       /* menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
            }
        });*/
    }

    public void displayNotificationWithDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                displayNotification();
            }
        }, 2000);

    }


    public void displayNotification() {
        NotificationCompat.Builder mBuilder;
        if (!diaryList.isEmpty()) {
            Diary diary = diaryList.get(diaryList.size() - 1);
            // Erstelle den Intent für das Speichern des Eintrags
            Intent intent = new Intent(getApplicationContext(), DiaryContent.class);
            intent.putExtra("diaryid", diary.getId());
            PendingIntent saveEntryPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            intent.setAction("SAVE_ENTRY_ACTION");
            Locale loc = new Locale("en", "US");
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, loc);


            NotificationCompat.Action.Builder actionBuilder = new NotificationCompat.Action.Builder(
                    R.drawable.ic_yoda,
                    "continue editing from last time-" + dateFormat.format(diary.getCreatedAt()),
                    saveEntryPendingIntent);
            mBuilder =
                    new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.baseline_deblur_24)
                            .setContentTitle("Mach einen Blog Eintrag")
                            .setContentText("Erstelle jetzt einen neuen Eintrag für deinen Blog")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true)
                            .addAction(actionBuilder.build())
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("Erstelle jetzt einen neuen Eintrag für deinen Blog"));
        } else {
            mBuilder =
                    new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.baseline_deblur_24)
                            .setContentTitle("Mach einen Blog Eintrag")
                            .setContentText("Erstelle jetzt einen neuen Eintrag für deinen Blog")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("Erstelle jetzt einen neuen Eintrag für deinen Blog"));
        }


        // Benachrichtigung anzeigen
        mNotificationMngr = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mNotificationMngr.notify(1, mBuilder.build());
    }

    public void initMenu() {
        menu = findViewById(R.id.menu);
        /*menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
            }
        });*/
        menu.setOnTouchListener(this);
    }

    public void initEintrag() {
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Erstelle einen AlertDialog Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(DiaryActivity.this);
                builder.setTitle("Neuen Eintrag hinzufügen");

                // Erstelle ein Eingabefeld für den neuen Eintrag
                final EditText input = new EditText(DiaryActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Füge Buttons für "Hinzufügen" und "Abbrechen" hinzu
                builder.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newEntry = input.getText().toString().trim();
                        if (!newEntry.isEmpty() && userid != -1) {
                            Diary diary = new Diary(userid, newEntry, "");
                            addDiaryEntry(diary);
                            diaryManager.addDiary(diary);
                            //diaryManager.deleteAllDiaries();
                        } else {
                            Toast.makeText(DiaryActivity.this, "cant be added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }


    public void addDiaryEntry(Diary diary) {
        diaryList.add(diary);
        diaryAdapter.notifyDataSetChanged();
    }

    public void deleteDiaryEntry(Diary diary) {
        if (diaryList.get(diaryList.size()-1)==diary) {
            mNotificationMngr.cancel(1);
        }
        diaryList.remove(diary);
        diaryAdapter.notifyDataSetChanged();
        if (diaryList.isEmpty()) {
            mNotificationMngr.cancel(1);
        }
    }

    public void showPopUp(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(this, "see you next time", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.konto:
                Intent i = getIntent();
                int userid = i.getIntExtra("userid", -1);
                Intent intent1 = new Intent(getApplicationContext(), KontoActivity.class);
                intent1.putExtra("userid", userid);
                startActivity(intent1);
                return true;
            case R.id.contact:
                Intent intent2 = new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(intent2);
                return true;
        }
        return false;
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
        startX = e.getX();
        startY = e.getY();
        System.out.println(startX + "" + startY);
        int position = diaryListView.pointToPosition((int) startX, (int) startY);
        Diary diary = diaryList.get(position);
        Intent intent = new Intent(getApplicationContext(), DiaryContent.class);
        intent.putExtra("diaryid", diary.getId());
        startActivity(intent);
        return false;
    }

    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {
        startX = e.getX();
        startY = e.getY();
        System.out.println("dwakfawnfoiag");
        int position = diaryListView.pointToPosition((int) startX, (int) startY);
        System.out.println(startX + " " + startY + position);
        if (position != AdapterView.INVALID_POSITION) {
            Diary diary = diaryList.get(position);
            deleteDiaryEntry(diary);
            diaryManager.deleteDiary(diary);
            Toast.makeText(DiaryActivity.this, "Entry deleted" + diary.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        startX = e1.getX();
        startY = e1.getY();
        System.out.println("dwakfawnfoiag");
        int position = diaryListView.pointToPosition((int) startX, (int) startY);
        System.out.println(startX + " " + startY + position);
        if (position != AdapterView.INVALID_POSITION) {
            Diary diary = diaryList.get(position);
            deleteDiaryEntry(diary);
            diaryManager.deleteDiary(diary);
            Toast.makeText(DiaryActivity.this, "Entry deleted" + diary.getName(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(this, "see you next time", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.konto:
                Intent i = getIntent();
                int userid = i.getIntExtra("userid", -1);
                Intent intent1 = new Intent(getApplicationContext(), KontoActivity.class);
                intent1.putExtra("userid", userid);
                startActivity(intent1);
                return true;
            case R.id.contact:
                Intent intent2 = new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(intent2);
                return true;
        }
        return false;
    }*/

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action;
       /* if (v.getId() == R.id.diaryList) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                startX = event.getX();
                startY = event.getY();
                int position = diaryListView.pointToPosition((int) startX, (int) startY);
                Diary diary = diaryList.get(position);
                Intent intent = new Intent(getApplicationContext(), DiaryContent.class);
                intent.putExtra("diaryid", diary.getId());
                startActivity(intent);
            }
        }*/
        switch (v.getId()) {
            case R.id.diaryList:
                gestureDetector.onTouchEvent(event);
                /*action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    startX = event.getX();
                    startY = event.getY();
                    System.out.println(startX + "" + startY);
                    int position = diaryListView.pointToPosition((int) startX, (int) startY);
                    Diary diary = diaryList.get(position);
                    Intent intent = new Intent(getApplicationContext(), DiaryContent.class);
                    intent.putExtra("diaryid", diary.getId());
                    startActivity(intent);
                   */
                return true;
            case R.id.menu:
                action = event.getAction();
                System.out.println("Menu");
                if (action == MotionEvent.ACTION_DOWN) {
                    showPopUp(v);
                }
                return false;
        }
        return true;
    }
}