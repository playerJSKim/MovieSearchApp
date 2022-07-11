package com.flow.NaverMovie_KJS.Network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.flow.NaverMovie_KJS.MovieDB.MovieInfo;

import java.util.HashMap;
import java.util.Map;

public class Connection {
//    String clientId = "Co1THiB4ZOaY6cvwv5yE"; // 네이버 검색 API 클라이언트 ID
//    String clientSecret = "l9jIEJXJET"; // 네이버 검색 API 클라이언트 시크릿
//
//    private makeRequest(RequestQueue requestQueue, String query) {
//        MovieInfo mI = new MovieInfo();
//        mI.title = query;
//        movieDatabase.getMovieDao().insert(mI);
//        inQueue(query);
//
//        String apiURL = "https://openapi.naver.com/v1/search/movie.json?query="+query;
//        StringRequest request = new StringRequest(Request.Method. GET , apiURL, new Response.Listener<String>() {
//            @Override public void onResponse(String response) {
//                return response;
////                processResponse(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("makeRequest", error.toString());
//            }
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("X-Naver-Client-Id",clientId);
//                params.put("X-Naver-Client-Secret",clientSecret);
//                return params;
//            }
//        };
//        request.setShouldCache(false);
//        requestQueue.add(request);
//    }
}
