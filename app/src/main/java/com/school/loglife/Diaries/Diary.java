package com.school.loglife.Diaries;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.school.loglife.Users.User;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "diary",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "userId",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE))
public class Diary {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private Date createdAt;
    private String content;
    private int userId;


    public Diary(int userId, String name, String content) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.name = name;
        this.createdAt = new Date();
        this.content = content;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}