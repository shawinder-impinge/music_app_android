package com.musicapp.localdatabase;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DownloadedSongsModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
