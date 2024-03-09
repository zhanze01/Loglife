package com.school.loglife.room;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.school.loglife.Diaries.Diary;
import com.school.loglife.Users.User;

@Database(entities = {Diary.class, User.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class DiaryDataBase extends RoomDatabase {

    public abstract DiaryDao diaryDao();

    public abstract UserDao userDao();

}
