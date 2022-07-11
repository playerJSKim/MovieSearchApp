package com.flow.NaverMovie_KJS.MovieDB;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert
    void insert(MovieInfo movieInfo);

    @Update
    void update(MovieInfo movieInfo);

    @Delete
    void delete(MovieInfo movieInfo);

    @Query("SELECT * FROM MovieInfo")
    List<MovieInfo> getAll();

    @Query("DELETE FROM MovieInfo")
    void clearAll();

    @Query("DELETE FROM MovieInfo WHERE id = 1")
    void deletedata();

}
