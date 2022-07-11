package com.flow.NaverMovie_KJS;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flow.NaverMovie_KJS.MovieDB.MovieDataManager;
import com.flow.NaverMovie_KJS.MovieDB.MovieDatabase;
import com.flow.NaverMovie_KJS.MovieDB.MovieInfo;
import com.flow.NaverMovie_KJS.ViewModel.MovieViewModel;
import com.flow.NaverMovie_KJS.ViewModel.MovieViewModelFactory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private MovieDatabase movieDatabase;
    MovieAdapter adapter;
    RecyclerView recyclerView;
    static RequestQueue requestQueue;
    String clientId = "Co1THiB4ZOaY6cvwv5yE"; // 네이버 검색 API 클라이언트 ID
    String clientSecret = "l9jIEJXJET"; // 네이버 검색 API 클라이언트 시크릿
    String uriString = "content://com.flow.NaverMovie_KJS/movie"; // 데이터 저장 url

    private ArrayList<String> recentarrayList = new ArrayList<String>();

    MovieViewModel movieViewModel;
    MovieDataManager movieDataManager = new MovieDataManager();;

    /** registerForActivityResult를 사용하기 위한 초기화 */
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
        if(result.getResultCode() == Activity.RESULT_OK){
            Intent data = result.getData();
            if(data != null){
                String getdata = data.getStringExtra("data");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                adapter.clearItems();
                makeRequest(getdata);

            }
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        adapter = new MovieAdapter();
        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        movieDatabase = Room.databaseBuilder(getActivity(),MovieDatabase.class,"movie").allowMainThreadQueries().build();
        movieViewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);


        Observer<String> title = newtitle -> makeRequest(newtitle);
        movieViewModel.getTitle().observe(getActivity(),title);

//        Observer<String> resdata = newdata ->

        final SearchView searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                //검색버튼이 눌렸을 때
                adapter.clearItems();

                movieViewModel.getTitle().setValue(query);

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //검색어가 변경되었을 때
                return false;
            }
        });

        /** 외부버튼 검색 연결*/
        final Button submitBtn = rootView.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                searchView.setQuery(searchView.getQuery(),true);
            }
        });
        final Button recentBtn = rootView.findViewById(R.id.recentBtn);
        recentBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), RecentSearch.class);
//                intent.putStringArrayListExtra("recentMovie",getRecent("recent")); //최근 검색 목록 전달
                launcher.launch(intent);
            }
        });

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void makeRequest(String query) {
        MovieInfo mI = new MovieInfo();
        mI.title = query;
        movieDatabase.getMovieDao().insert(mI);
        movieDataManager.init(getActivity());
        movieDataManager.inQueue(query);

        String apiURL = "https://openapi.naver.com/v1/search/movie.json?query="+query;
        StringRequest request = new StringRequest(Request.Method. GET , apiURL, new Response.Listener<String>() {
            @Override public void onResponse(String response) {
                processResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("makeRequest", error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("X-Naver-Client-Id",clientId);
                params.put("X-Naver-Client-Secret",clientSecret);
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    /**
     * 검색된 영화들을 리스트에 등록
     * @param response
     */
    private void processResponse(String response){
        Gson gson = new Gson();
        MovieList movieList = gson.fromJson(response, MovieList.class);

        for(int i = 0; i < movieList.items.size(); i++){
            Movie movie = movieList.items.get(i);
            adapter.addItem(movie);
        }
        adapter.notifyDataSetChanged();
    }

}
