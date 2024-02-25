package com.school.loglife.Diaries;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "diary")
public class Diary {
    public String id;
    public String name;
    public Date createdAt;
    public String content;

    public Diary(String name, String content) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.createdAt = new Date();
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @Override
    public String toString() {
        return "Diary{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
