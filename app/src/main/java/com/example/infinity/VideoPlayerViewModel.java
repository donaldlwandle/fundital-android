package com.example.infinity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VideoPlayerViewModel extends ViewModel {

    private final MutableLiveData<CharSequence> videoUrl = new MutableLiveData<>();

    public void setVideoUrl(CharSequence url){
        videoUrl.setValue(url);
    }

    public LiveData<CharSequence> getVideoUrl() {
        return videoUrl ;
    }
}
