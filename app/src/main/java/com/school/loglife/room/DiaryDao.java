package com.school.loglife.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.school.loglife.Diaries.Diary;

import java.util.List;

@Dao
public interface DiaryDao {
    @Insert
    void insertDiary(Diary diary);

    // einzelnen Task in der Datenbank updaten
    @Update
    void updateDiary(Diary diary);

    // Alle Datenpunkte der Tabelle "task_table" zurückgeben
    @Query("SELECT * FROM diary")
    List<Diary> getAllDiary();
}
