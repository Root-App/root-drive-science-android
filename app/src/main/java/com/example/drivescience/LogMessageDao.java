package com.example.drivescience;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LogMessageDao {
    @Query("SELECT * FROM logmessage ORDER BY uid")
    List<LogMessage> getAll();

    @Insert
    void insert(LogMessage logMessage);

    @Delete
    void delete(LogMessage logMessage);

    @Query("DELETE FROM logmessage")
    void deleteAll();
}
