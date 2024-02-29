package com.school.loglife.Diaries;

import android.content.Context;

import com.school.loglife.room.DiaryDataBaseHelper;

import java.util.List;

public class DiaryManager {

    private DiaryDataBaseHelper db;

    public DiaryManager(Context context) {
        this.db = new DiaryDataBaseHelper(context);
    }

    public void addDiary(Diary diary) {
        db.addDiary(diary);
    }

    public List<Diary> getAllDiaries(int userid) {
        return db.getAllTasks(userid);
    }

    public void deleteDiary(Diary diary) {
        db.deleteDiary(diary);
    }

}
