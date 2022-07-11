package com.flow.NaverMovie_KJS.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MovieViewModel extends ViewModel {
    private MutableLiveData<String> MovieTitle;
    private MutableLiveData<String> ResponseData;

    public MutableLiveData<String> getTitle() {
        if(MovieTitle == null){
            MovieTitle = new MutableLiveData<String>();
        }
        return MovieTitle;
    }

    public MutableLiveData<String> getResponseData() {
        if(ResponseData == null){
            ResponseData = new MutableLiveData<String>();
        }
        return ResponseData;
    }
}
