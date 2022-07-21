package com.example.infinity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DiscussionDCarierModel extends ViewModel {

    private final MutableLiveData<String> discussionID = new MutableLiveData<>();

    public void setDiscussionID(String stringID){
        discussionID.setValue(stringID);
    }

    public LiveData<String> getDiscussionID(){
        return discussionID ;
    }


}
