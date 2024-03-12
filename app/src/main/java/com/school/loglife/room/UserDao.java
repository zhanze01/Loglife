package com.school.loglife.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.school.loglife.Users.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("Delete FROM users")
    void deleteAll();

    @Update
    void update(User user);

    @Query("SELECT * FROM users WHERE userid = :userId")
    User getUserById(int userId);

    @Query("select * from users where username=:username")
    User getUserByUsername(String username);

    @Query("select * from users where username=:username and password=:password")
    User getUserByUsernameAndPassword(String username, String password);

}
