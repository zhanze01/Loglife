package com.school.loglife;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.school.loglife.Diaries.Diary;
import com.school.loglife.Diaries.DiaryManager;
import com.school.loglife.UI.DiaryAdapter;

import java.util.ArrayList;
import java.util.List;

public class DiaryActivity extends AppCompatActivity {

    private ListView diaryListView;
    //private ArrayList<String> diaryEntries;
    //private ArrayAdapter<String> diaryAdapter;
    private DiaryManager diaryManager;
    private List<Diary> diaryList;
    private DiaryAdapter diaryAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        diaryListView = findViewById(R.id.diaryList);
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

                // Setze die Hintergrundfarbe für das Eingabefeld
                input.setBackgroundResource(android.R.drawable.edit_text);

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


}