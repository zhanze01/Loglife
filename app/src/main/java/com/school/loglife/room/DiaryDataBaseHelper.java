package com.school.loglife.room;


import android.content.Context;

import androidx.room.Room;

import com.school.loglife.Diaries.Diary;
import com.school.loglife.Users.User;

import java.util.ArrayList;
import java.util.List;

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

    public Diary findDiaryById(String diaryid) {
        return db.diaryDao().getDiary(diaryid);
    }

    // alle in der Datenbank existierenden Tasks holen
    public List<Diary> getAllTasks(int userid) {
        return db.diaryDao().getAllDiary(userid);
    }

    public void deleteDiary(Diary diary) {
        db.diaryDao().deleteDiary(diary);
    }

    public void addUser(User user) {
        db.userDao().insert(user);
    }

    public void deleteAll() {
        db.userDao().deleteAll();
    }

    public void deleteAllDiaries() {
        db.diaryDao().deleteAll();
    }

    public User findUser(String username, String password) {
        return db.userDao().getUserByUsernameAndPassword(username, password);
    }

    public User findUser(String username) {
        return db.userDao().getUserByUsername(username);
    }

    public User findUserById(int userid) {
        return db.userDao().getUserById(userid);
    }

    public void updateUser(User user) {
        db.userDao().update(user);
    }

}