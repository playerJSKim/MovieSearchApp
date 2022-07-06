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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    MovieAdapter adapter;
    RecyclerView recyclerView;
    static RequestQueue requestQueue;

    String clientId = "Co1THiB4ZOaY6cvwv5yE"; // 네이버 검색 API 클라이언트 ID
    String clientSecret = "l9jIEJXJET"; // 네이버 검색 API 클라이언트 시크릿
    String uriString = "content://com.flow.NaverMovie_KJS/movie"; // 데이터 저장 url

    private ArrayList<String> recentarrayList = new ArrayList<String>();

    /** registerForActivityResult를 사용하기 위한 초기화 */
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
        if(result.getResultCode() == Activity.RESULT_OK){
            Intent data = result.getData();
            if(data != null){
                String getdata = data.getStringExtra("data");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(this).attach(this).commit();
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

        /** 프래그먼트로 부터 넘겨받은 데이터가 있으면 바로 검색*/
        Bundle bundle=getArguments();
        if(bundle != null){
            String senddata = bundle.getString("data");
            makeRequest(senddata);
        }

        final SearchView searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                //검색버튼이 눌렸을 때
                adapter.clearItems();
                makeRequest(query);
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
                intent.putStringArrayListExtra("recentMovie",getRecent("recent")); //최근 검색 목록 전달
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
    private void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
    private void makeRequest(String query) {
        inQueue(query);

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

    /**
     * ArrayList를 Queue로 사용하여 최근 검색된 순서로 검색어를 저장 및 관리
     * 최대 10개의 검색어 저장
     * @param data
     */
    public void inQueue(String data){
        ArrayList<String> testarr = getRecent("recent");
        if(testarr.size()>0){
            for(int i=0; i<testarr.size(); i++){
                if(data.compareTo(testarr.get(i)) == 0){
                    testarr.remove(i);
                }
            }
        }
        if(testarr.size() < 10){
            testarr.add(data);
            putRecent("recent",testarr);


        }else if(testarr.size() >= 10){
            testarr.remove(0);
            testarr.add(data);
            putRecent("recent",testarr);
        }
    }

    /**
     * SharedPreferences에 ArrayList 저장(영화 검색 목록)
     * @param key
     * @param values
     */
    private void putRecent(String key, ArrayList<String> values) {
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sf.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    /**
     * SharedPreferences에 ArrayList 불러오기(영화 검색 목록)
     * @param key
     * @return
     */
    private ArrayList<String> getRecent(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }



}
