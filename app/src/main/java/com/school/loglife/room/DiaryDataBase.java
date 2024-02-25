package com.school.loglife.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.school.loglife.Diaries.Diary;

@Database(entities = {Diary.class}, version = 1)
public abstract class DiaryDataBase extends RoomDatabase {

    public abstract DiaryDao diaryDao();

}
