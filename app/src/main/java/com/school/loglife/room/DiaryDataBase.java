package com.school.loglife.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.school.loglife.Diaries.Diary;
import com.school.loglife.Users.User;

@Database(entities = {Diary.class, User.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class DiaryDataBase extends RoomDatabase {

    public abstract DiaryDao diaryDao();

    public abstract UserDao userDao();

}
