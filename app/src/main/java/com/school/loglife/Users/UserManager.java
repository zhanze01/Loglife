package com.school.loglife.Users;

import android.content.Context;

import com.school.loglife.room.DiaryDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private DiaryDataBaseHelper db;

    public UserManager(Context context) {
        this.db = new DiaryDataBaseHelper(context);
    }

    public void addUser(String username, String password) {
        User user = new User(username, password);
        db.addUser(user);
    }

    public void deleteAll() {
        db.deleteAll();
    }

    public User getUser(String username, String password) {
        return db.findUser(username, password);
    }

    public User getUser(String username) {
        return db.findUser(username);
    }

}
