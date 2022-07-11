package com.flow.NaverMovie_KJS.MovieDB;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class MovieDataManager {

    private MovieDatabase movieDatabase;

    public void init(Context context){
        movieDatabase = Room.databaseBuilder(context,MovieDatabase.class,"movie").allowMainThreadQueries().build();
    }
    public void inQueue(String data){
        List<MovieInfo> list = movieDatabase.getMovieDao().getAll();
        ArrayList<String> testarr = new ArrayList<>();
        String str = "";
        if(list.size()>0){
            for(MovieInfo movieInfo : list){
                if(!str.equals(movieInfo.title)){
                    testarr.add(movieInfo.title);
                    str = movieInfo.title;
                }
            }
            movieDatabase.getMovieDao().clearAll();
        }
        if(testarr.size()>0){
            for(int i=0; i<testarr.size(); i++){
                if(data.compareTo(testarr.get(i)) == 0 || data.equals(testarr.get(i))){
                    testarr.remove(data);

                }
            }
        }
        if(testarr.size() < 10){
            testarr.add(data);
//            putRecent("recent",testarr);
            MovieInfo movieInfo = new MovieInfo();
            for(int i=0; i<testarr.size(); i++){
                movieInfo.title = testarr.get(i);
                movieDatabase.getMovieDao().insert(movieInfo);
            }

        }else if(testarr.size() >= 10){
            testarr.remove(0);
            testarr.add(data);
//            putRecent("recent",testarr);
            MovieInfo movieInfo = new MovieInfo();
            for(int i=0; i<testarr.size(); i++){
                movieInfo.title = testarr.get(i);
                movieDatabase.getMovieDao().insert(movieInfo);
            }
        }

    }


}
