package com.example.infinity.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infinity.Models.database.Courses;
import com.example.infinity.R;
import com.example.infinity.Utils.CoursesAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    /*widgets*/
    private View homeView ;
    private RecyclerView subjectsList , accountingSubjectsList , physicsSubjectsList , chemistrySubjectsList;
    private CoursesAdapter adapter , accountingAdapter ;
    private RelativeLayout searchBar ;
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout contentLayout;

    /*Vars*/


    /*Firebase*/
    private DatabaseReference myRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: HomeFragment Starting");
        homeView = inflater.inflate(R.layout.fragment_home , container ,false);
        initialize();
        setupCourses();


        return homeView ;
    }



    private void  initialize(){
        subjectsList = homeView.findViewById(R.id.home_recycler_view);
        accountingSubjectsList = homeView.findViewById(R.id.accounting_recycler_view);
        physicsSubjectsList = homeView.findViewById(R.id.physics_recycler_view);
        shimmerFrameLayout = homeView.findViewById(R.id.home_shimmer_layout);
        contentLayout = homeView.findViewById(R.id.home_content_view);

        searchBar = homeView.findViewById(R.id.search_bar);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.search_action);
            }
        });

        myRef = FirebaseDatabase.getInstance().getReference() ;

        shimmerFrameLayout.startShimmer();



    }





    private void setupCourses(){

        ArrayList<Courses> mathsList = new ArrayList<>();
        ArrayList<Courses> accountingList = new ArrayList<>();
        ArrayList<Courses> physicsList = new ArrayList<>();

        myRef.child("courses")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                        for (DataSnapshot singleSnapShot : dataSnapshot.getChildren() ){


                            if (singleSnapShot.child("field").getValue().equals("mathematics")){
                                mathsList.add(singleSnapShot.getValue(Courses.class));

                            }else if (singleSnapShot.child("field").getValue().equals("accounting")){
                                accountingList.add(singleSnapShot.getValue(Courses.class));

                            } else {

                                physicsList.add(singleSnapShot.getValue(Courses.class));
                            }
                        }

                        setupMathematics(mathsList);
                        setupAccounting(accountingList);
                        setupPhysics(physicsList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });








    }



    private void setupMathematics(ArrayList<Courses> mathsList) {

        try {

            adapter = new CoursesAdapter(mathsList , getContext());

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            subjectsList.setLayoutManager(layoutManager);
            subjectsList.hasFixedSize();
            subjectsList.setAdapter(adapter);

        }catch (NullPointerException e){
            Log.e(TAG, "setupMathematics: NullPointerException : " + e.getMessage());
        }




    }




    private void setupAccounting(ArrayList<Courses> accountingList) {


        try {

            accountingAdapter = new CoursesAdapter(accountingList , getContext() );

            LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
            layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
            accountingSubjectsList.setLayoutManager(layoutManager2);
            accountingSubjectsList.setAdapter(accountingAdapter);

        }catch (NullPointerException e){
            Log.e(TAG, "setupAccounting: NullPointerException : " + e.getMessage());
        }



    }


    private void setupPhysics(ArrayList<Courses> physicsList) {


        try {

            CoursesAdapter physicsAdapter = new CoursesAdapter(physicsList, getContext() );

            LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext());
            layoutManager3.setOrientation(RecyclerView.HORIZONTAL);
            physicsSubjectsList.setLayoutManager(layoutManager3);
            physicsSubjectsList.setAdapter(physicsAdapter);


            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setShimmer(null);
            contentLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.setVisibility(View.GONE);



        }catch (NullPointerException e){

            Log.e(TAG, "setupPhysics: NullPointerException : " + e.getMessage() );

        }


    }


}
