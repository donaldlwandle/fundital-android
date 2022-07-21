package com.example.infinity.home.course;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infinity.CourseGetterViewModel;
import com.example.infinity.Models.database.Chapters;
import com.example.infinity.Models.database.Courses;
import com.example.infinity.R;
import com.example.infinity.Utils.ChaptersAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WatchFragment extends Fragment {
    private static final String TAG = "WatchFragment";

    /*widgets*/
    private View watchView ;
    private RecyclerView videoChaptersRecycler ;
    private RelativeLayout noContentView;

    /*Adapters*/
    private ChaptersAdapter chaptersAdapter;

    private DatabaseReference myRef ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: watchFragment Starting");
        watchView = inflater.inflate(R.layout.fragment_watch , container ,false);
        initialize();

        /*setup video chapters container*/
        getCourseIDsFromParent();
        
        return  watchView ;
    }


    private  void  initialize(){
        videoChaptersRecycler = watchView.findViewById(R.id.chapters_video_container);
        noContentView = watchView.findViewById(R.id.no_content_view);
        /*initialise firebase vars*/
        /*firebase*/
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


    }

    private void displayCourseContentChapters(Courses mCourse){

        myRef.child("institutions_courses").child(mCourse.getInstitution_id())
                .child(mCourse.getCourse_id())
                .child("chapters")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Chapters> list = new ArrayList<>();

                        for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()){
                            list.add(singleSnapShot.getValue(Chapters.class));
                        }


                        /*adapter*/
                        chaptersAdapter = new ChaptersAdapter(list ,getContext());
                        /*setup layout manager*/
                        videoChaptersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                        videoChaptersRecycler.setHasFixedSize(true);
                        /*setup adapter*/
                        videoChaptersRecycler.setAdapter(chaptersAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




    }


    /*Getting course ID's From LiveData View Model updated when navigating to the current course
     * View Model is CourseGetterViewModel*/
    private void getCourseIDsFromParent(){
        /*View Models*/
        CourseGetterViewModel courseGetterViewModel = new ViewModelProvider(requireActivity()).get(CourseGetterViewModel.class);
        courseGetterViewModel.getCourse().observe(getViewLifecycleOwner(), new Observer<Courses>() {
            @Override
            public void onChanged(Courses courses) {
                /*Getting Course Information*/
                try {
                    noContentView.setVisibility(View.GONE);
                    displayCourseContentChapters(courses);
                }catch (NullPointerException e){
                    Log.e(TAG, "onChanged: NullPointerException : " +e.getMessage()  );
                    noContentView.setVisibility(View.VISIBLE);
                }
            }
        });



    }
}
