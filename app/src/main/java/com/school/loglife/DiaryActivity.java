package com.school.loglife;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DiaryActivity extends AppCompatActivity {

    private ListView diaryListView;
    private ArrayList<String> diaryEntries;
    private ArrayAdapter<String> diaryAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        diaryListView = findViewById(R.id.diaryList);
        diaryEntries = new ArrayList<>();
        diaryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, diaryEntries);

        diaryListView.setAdapter(diaryAdapter);

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
                        if (!newEntry.isEmpty()) {
                            addDiaryEntry(newEntry);
                        } else {
                            Toast.makeText(DiaryActivity.this, "Eintrag darf nicht leer sein", Toast.LENGTH_SHORT).show();
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

        diaryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                String entryToDelete = diaryEntries.get(position);
                diaryEntries.remove(position);
                diaryAdapter.notifyDataSetChanged();
                Toast.makeText(DiaryActivity.this, "Entry deleted" + entryToDelete, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        }
        private void addDiaryEntry(String entry){
            diaryEntries.add(entry);
            diaryAdapter.notifyDataSetChanged();
    }
}