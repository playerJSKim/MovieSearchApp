package com.flow.NaverMovie_KJS.MovieDB;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MovieInfo.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao getMovieDao();
}
