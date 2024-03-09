package com.school.loglife;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.school.loglife.Diaries.Diary;
import com.school.loglife.Diaries.DiaryManager;
import com.school.loglife.UI.DiaryAdapter;

import java.util.ArrayList;
import java.util.List;

public class DiaryActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private ListView diaryListView;
    private DiaryManager diaryManager;
    private List<Diary> diaryList;
    private DiaryAdapter diaryAdapter;
    private ImageView menu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        diaryListView = findViewById(R.id.diaryList);
        menu = findViewById(R.id.menu);
        diaryManager = new DiaryManager(getApplicationContext());
        Intent intent = getIntent();
        int userid = intent.getIntExtra("userid", -1);
        List<Diary> diaries = diaryManager.getAllDiaries(userid);
        if (diaries != null) {
            diaryList = diaryManager.getAllDiaries(userid);
        } else {
            diaryList = new ArrayList<>();
        }
        diaryAdapter = new DiaryAdapter(this, android.R.layout.simple_list_item_1, diaryList);
        diaryListView.setAdapter(diaryAdapter);
        diaryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
            }
        });

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

                // Zeige den AlertDialog an
                builder.show();
            }
        });
    }

    private void addDiaryEntry(Diary diary) {
        diaryList.add(diary);
        diaryAdapter.notifyDataSetChanged();
    }

    private void deleteDiaryEntry(Diary diary) {
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
}