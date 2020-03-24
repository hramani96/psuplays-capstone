package com.example.psuplays;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface adminsDAO {

    @Query("SELECT * FROM admins WHERE rowid = :adminId")
    Admin getById(int adminId);

    @Insert
    void insert(Admin...admins);

    @Update
    void update(Admin... admin);

    @Delete
    void delete(Admin... user);

    @Query("DELETE FROM admins WHERE rowid = :adminId")
    void delete(int adminId);

}
