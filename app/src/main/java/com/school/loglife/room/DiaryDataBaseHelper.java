package com.school.loglife.room;

import android.content.Context;

import androidx.room.Room;

import com.school.loglife.Diaries.Diary;

import java.util.ArrayList;

public class DiaryDataBaseHelper {
    private static final String DATABASE_NAME = "diary-db";
    private Context context;
    private DiaryDataBase db;

    public DiaryDataBaseHelper(Context context) {
        this.context = context;
        initDatabase();
    }

    private void initDatabase() {
        // Erstellen der Datenbank; benötigt werden Kontext, Klasse der Datenbank, die man erstellen will und der Name der Datenbank
        db = Room.databaseBuilder(context, DiaryDataBase.class, DATABASE_NAME)
                // ermöglicht das Ausführen von Datenbankabfragen im MainThread (wird nicht empfohlen!)
                .allowMainThreadQueries()
                .build();
    }

    // einzelnen Task zur Datenbank hinzufügen
    public void addDiary(Diary diary) {
        db.diaryDao().insertDiary(diary);
    }

    // bestehenden Task in der Datenbank updaten
    public void update(Diary diary) {
        db.diaryDao().updateDiary(diary);
    }

    // alle in der Datenbank existierenden Tasks holen
    public ArrayList<Diary> getAllTasks() {
        return new ArrayList<Diary>(db.diaryDao().getAllDiary());
    }
}
