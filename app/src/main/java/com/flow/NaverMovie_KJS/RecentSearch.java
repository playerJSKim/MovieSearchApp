package com.flow.NaverMovie_KJS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.flow.NaverMovie_KJS.MovieDB.MovieDatabase;
import com.flow.NaverMovie_KJS.MovieDB.MovieInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecentSearch extends AppCompatActivity {
    Context context;
    private ArrayList<String> arr_movie;
    private ListView mListView;
    private TextView tv;
    private LinearLayout container;
    private static final float FONT_SIZE = 20;
    private MovieDatabase movieDatabase;
    final ArrayList<String> recentmovie = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_search);

        context = getApplicationContext();
        container = findViewById(R.id.container);
        arr_movie = new ArrayList<>();
        movieDatabase = Room.databaseBuilder(this, MovieDatabase.class,"movie").allowMainThreadQueries().build();

        List<MovieInfo> list = movieDatabase.getMovieDao().getAll();
        for(MovieInfo movieInfo : list){
            recentmovie.add(movieInfo.title);
        }

        if (recentmovie != null) {
            Collections.reverse(recentmovie);
        }

        if (recentmovie != null && !recentmovie.isEmpty()) {
            for (int i = 0; i < recentmovie.size(); i++) {
                Createtextview(recentmovie.get(i));
            }
        }
    }

/**
 * Textview 동적 생성
 */
    public void Createtextview(final String a){
        final String data = a;
        //TextView 생성
        TextView view1 = new TextView(this);
        view1.setText(a);
        view1.setTextSize(FONT_SIZE);
        view1.setTextColor(Color.BLACK);
        view1.setPadding(70,30,0,10);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.LEFT;
        view1.setLayoutParams(lp);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("data",data);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        //부모 뷰에 추가
        container.addView(view1);




    }

}