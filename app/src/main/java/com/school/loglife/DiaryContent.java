package com.school.loglife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.school.loglife.Diaries.Diary;
import com.school.loglife.Diaries.DiaryManager;
import com.school.loglife.Users.UserManager;

public class DiaryContent extends AppCompatActivity {
    private DiaryManager manager;
    private EditText edit;
    private Button save;
    private Button leave;
    private Diary diary;
    private Intent intent;
    private TextView error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_content);
        init();

    }

    public void init() {
        edit = (EditText) findViewById(R.id.content);
        save = (Button) findViewById(R.id.save);
        leave = (Button) findViewById(R.id.leave);
        error = (TextView) findViewById(R.id.error);
        manager = new DiaryManager(getApplicationContext());
        intent = getIntent();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });

        save.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_BUTTON_PRESS:
                        edit();
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

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String diaryid = intent.getStringExtra("diaryid");
        if (diaryid != null && !diaryid.equals("")) {
            diary = manager.findDiary(diaryid);
            edit.setText(diary.getContent());
        } else {
            error.setTextColor(Color.RED);
            error.setText("could not find content");
        }
    }

    public void edit() {
        String content = edit.getText().toString();
        if (content != null && !content.equals("")) {
            String diaryid = intent.getStringExtra("diaryid");
            if (diaryid != null && !diaryid.equals("")) {
                diary = manager.findDiary(diaryid);
                diary.setContent(content);
                manager.updateDiary(diary);
                error.setTextColor(Color.GREEN);
                error.setText("content saved and updated");
            } else {
                error.setTextColor(Color.RED);
                error.setText("could not find content");
            }
        } else {
            error.setTextColor(Color.RED);
            error.setText("content empty");
        }
    }
}