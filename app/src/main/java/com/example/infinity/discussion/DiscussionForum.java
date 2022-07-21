package com.example.infinity.discussion;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infinity.CourseGetterViewModel;
import com.example.infinity.Models.database.Courses;
import com.example.infinity.Models.database.Discussions;
import com.example.infinity.Models.database.Votes;
import com.example.infinity.R;
import com.example.infinity.Utils.DiscussionsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscussionForum extends Fragment {
    private static final String TAG = "DiscussionForum";

    /*Widgets*/
    private View view;
    private RecyclerView discussionsListView;
    /*Adapters*/
    private DiscussionsAdapter adapter ;

    /*firebase*/
    private FirebaseDatabase mFirebaseDatabase ;
    private DatabaseReference myRef ;



    /*Vars*/


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: DiscussionForum Starting");

        view = inflater.inflate(R.layout.fragment_discussion_forum, container , false);
        initialize();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCourseIDsFromParent();
    }

    private void initialize(){

        /*back button*/

        discussionsListView = view.findViewById(R.id.discussion_recycler);

        /*initialise firebase vars*/
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

    }

    private void displayingDiscussions(Courses mCourse){



        myRef.child("institutions_courses").child(mCourse.getInstitution_id())
                .child(mCourse.getCourse_id())
                .child("discussions")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Discussions> discussionsList = new ArrayList<Discussions>();

                        for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()){

                            Discussions discussion = new Discussions();
                            List<Votes> votesList = new ArrayList<Votes>() ;
                            Map<String ,Object> objectMap = (HashMap<String, Object>) singleSnapShot.getValue() ;
                            discussion.setDiscussion(objectMap.get(getContext().getString(R.string.discussion)).toString());
                            discussion.setUser_id(objectMap.get(getContext().getString(R.string.user_id)).toString());
                            discussion.setUsername(objectMap.get(getContext().getString(R.string.username)).toString());
                            discussion.setProfile_pic(objectMap.get(getContext().getString(R.string.profile_pic)).toString());
                            discussion.setDate_created(objectMap.get(getContext().getString(R.string.date_created)).toString());
                            discussion.setInstructor(objectMap.get(getContext().getString(R.string.instructor)).toString());
                            discussion.setDiscussion_id(objectMap.get(getContext().getString(R.string.discussion_id)).toString());

                            for (DataSnapshot dSnapShot : singleSnapShot.child("votes").getChildren()){

                                Votes newVotes = new Votes() ;
                                newVotes.setUser_id(dSnapShot.getValue(Votes.class).getUser_id());
                                votesList.add(newVotes);

                            }
                            discussion.setVotes(votesList);



                            discussionsList.add(discussion);
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        discussionsListView.setLayoutManager(linearLayoutManager);
                        discussionsListView.setHasFixedSize(true);


                        adapter= new DiscussionsAdapter(discussionsList , getContext() ,mCourse.getInstitution_id() ,mCourse.getCourse_id());
                        discussionsListView.setAdapter(adapter);

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
                    displayingDiscussions(courses);
                }catch (NullPointerException e){
                    Log.e(TAG, "onChanged: NullPointerException : " +e.getMessage()  );
                }
            }
        });



    }

}
