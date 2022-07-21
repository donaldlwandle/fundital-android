package com.example.infinity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.infinity.Models.database.Courses;

public class CourseGetterViewModel extends ViewModel {
    private final MutableLiveData<Courses> course = new MutableLiveData<>();
    public void setCourse(Courses courseInfo){
        course.setValue(courseInfo);
    }

    public LiveData<Courses> getCourse(){
        return course ;
    }
}
