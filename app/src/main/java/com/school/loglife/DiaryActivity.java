package com.school.loglife;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.school.loglife.Diaries.Diary;
import com.school.loglife.Diaries.DiaryManager;
import com.school.loglife.UI.DiaryAdapter;

import java.util.ArrayList;
import java.util.List;

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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        });*/

       /* menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
            }
        });*/
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
        diaryList.remove(diary);
        diaryAdapter.notifyDataSetChanged();
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