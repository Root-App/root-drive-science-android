package com.example.drivescience;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class LogMessage {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "message")
    public String message;

    @ColumnInfo(name = "date")
    public Date date;
}
