package com.example.infinity.home.course;



import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.infinity.CourseGetterViewModel;
import com.example.infinity.Models.database.Courses;
import com.example.infinity.Models.database.courseinfo.CourseInstructors;
import com.example.infinity.Models.database.courseinfo.CourseSkills;
import com.example.infinity.R;
import com.example.infinity.Utils.CourseInfoAdapters;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CourseInfoFragment extends Fragment {
    private static final String TAG = "CourseInfoFragment";

    /*Widgets*/
    View view;
    private RecyclerView skillsRecycler  , instructorsRecycler;
    private TextView courseAbout , coursePrerequisites, courseLanguage ;

    /*Firebase*/
    private DatabaseReference myRef ;


    /*Adapters*/
    private CourseInfoAdapters.CourseSkillsAdapter skillsAdapter;
    private CourseInfoAdapters.CourseInstructorsAdapter instructorsAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");
        view = inflater.inflate(R.layout.fragment_course_info , container , false);
        initialize();





        return view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*getting course ID's*/
        getCourseIDsFromParent();


    }

    private void initialize(){
        /*chaptersRecycler = view.findViewById(R.id.chapters_recycler);*/
        skillsRecycler = view.findViewById(R.id.skills_recycler);
        instructorsRecycler = view.findViewById(R.id.instructors_recycler);
        courseAbout = view.findViewById(R.id.course_about);
        coursePrerequisites = view.findViewById(R.id.course_prerequisites);
        courseLanguage = view.findViewById(R.id.course_language);

        myRef = FirebaseDatabase.getInstance().getReference();

    }



    /*method for setting up Skills recycler*/
    private void setupSkillsContainer(){
        List<CourseSkills> courseSkillsList = new ArrayList<>();
        CourseSkills skills1 = new CourseSkills();
        skills1.SKILL = "Problem Solving";

        CourseSkills skills2 = new CourseSkills();
        skills2.SKILL = "Auditing";

        CourseSkills skills3 = new CourseSkills();
        skills3.SKILL = "Analytical";

        CourseSkills skills4 = new CourseSkills();
        skills4.SKILL = "Critical Thinking";

        courseSkillsList.add(skills1);
        courseSkillsList.add(skills2);
        courseSkillsList.add(skills3);
        courseSkillsList.add(skills4);
        courseSkillsList.add(skills2);

        skillsAdapter = new CourseInfoAdapters.CourseSkillsAdapter(getContext() , courseSkillsList);

        /*setup layout manager*/
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);

        skillsRecycler.setLayoutManager(layoutManager);


        /*set adapter*/
        skillsRecycler.setAdapter(skillsAdapter);


    }


    /* method for setting up course instructor */
    private void setupInstructorsContainer( Courses mCourse){
        List<CourseInstructors> instructorsList = new ArrayList<>();

        myRef.child("institutions_courses").child(mCourse.getInstitution_id())
                .child(mCourse.getCourse_id())
                .child("instructors")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            instructorsList.add(singleSnapshot.getValue(CourseInstructors.class));
                        }

                        /*adapter*/
                        instructorsAdapter = new CourseInfoAdapters.CourseInstructorsAdapter(getContext() , instructorsList);

                        /*Layout Manager*/
                        LinearLayoutManager instructorsLayoutManager = new LinearLayoutManager(getContext());
                        instructorsLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        instructorsRecycler.setLayoutManager(instructorsLayoutManager);


                        /*adapt the recycler*/
                        instructorsRecycler.setAdapter(instructorsAdapter);

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
                populateCourseInformation(courses);

            }
        });
    }


    private void populateCourseInformation(Courses details){
        //set about
        courseAbout.setText(details.getAbout());

        //set prerequisites
        coursePrerequisites.setText(details.getPrerequisites());

        //set language
        courseLanguage.setText(details.getLanguage());

        /*setting up instructors adapter*/
        setupInstructorsContainer(details);

        /*setting up skills container*/
        setupSkillsContainer();

    }
}
