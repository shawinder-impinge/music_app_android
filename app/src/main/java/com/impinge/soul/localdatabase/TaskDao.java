package com.impinge.soul.localdatabase;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM downloadedsongsmodel")
    List<DownloadedSongsModel> getAll();

    @Insert
    void insert(DownloadedSongsModel task);

    @Delete
    void delete(DownloadedSongsModel task);

    @Update
    void update(DownloadedSongsModel task);

    @Query("DELETE FROM downloadedsongsmodel")
    void deleteSong();


}
