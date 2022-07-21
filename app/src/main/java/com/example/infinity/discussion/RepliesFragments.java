package com.example.infinity.discussion;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.infinity.CourseGetterViewModel;
import com.example.infinity.DiscussionDCarierModel;
import com.example.infinity.Models.database.Courses;
import com.example.infinity.Models.database.Replies;
import com.example.infinity.Models.database.Reviews;
import com.example.infinity.Models.database.UserData;
import com.example.infinity.R;
import com.example.infinity.Utils.Methods;
import com.example.infinity.Utils.RepliesAdapter;
import com.example.infinity.Utils.ReviewsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class RepliesFragments extends Fragment {

    private static final String TAG = "ReviewsFragments";

    /*widgets*/
    private View view ;
    private ImageView mBackButton ;
    private RecyclerView mRecyclerView ;
    private ProgressBar progressBar ;
    private CircleImageView currentUserProfile , sendReplyButton ;
    private EditText reply ;


    /*firebase*/
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase ;
    private DatabaseReference myRef ;

    //vars
    private Courses course;
    private UserData profileDetails ;
    private String mDiscussionID ;


    /*Adapter*/
    private RepliesAdapter adapter ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reviews , container , false);

        // initialize widgets
        initialize();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCourseIDsFromParent();
        getUserDetails();
    }


    /*method for initializing widgets*/
    private void initialize(){

        mBackButton = view.findViewById(R.id.replies_back_button);
        mRecyclerView = view.findViewById(R.id.replies_list_view);
        progressBar = view.findViewById(R.id.login_progress_bar);
        currentUserProfile = view.findViewById(R.id.replies_user_profile_pic);
        reply = view.findViewById(R.id.reply);
        sendReplyButton = view.findViewById(R.id.send_reply_button);

        /*back navigation*/
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });



        /*initialise firebase vars*/
        mAuth = FirebaseAuth.getInstance() ;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

    }

    /*method for setting up recyclerView*/
    private void setupReviewsList(String mDiscussionID){

        Query reviewsQuery = myRef.child("institutions_courses")
                .child(course.getInstitution_id())
                .child(course.getCourse_id())
                .child("discussions")
                .child(mDiscussionID)
                .child("replies");

        reviewsQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Replies> list = new ArrayList<>();

                        if (dataSnapshot.exists()){
                            for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()){
                                list.add(singleSnapShot.getValue(Replies.class));
                            }

                            /*setup reviews recycler view layout manager*/
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            mRecyclerView.setHasFixedSize(true);

                            /*set adapter to recyclerView*/
                            adapter = new RepliesAdapter(list , getContext());
                            mRecyclerView.setAdapter(adapter);
                        }



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
                course = courses;

                getDiscussionID();

                setupReplyUpdate();
            }
        });
    }

    private void getDiscussionID(){
        DiscussionDCarierModel discussionDCarierModel = new ViewModelProvider(requireActivity()).get(DiscussionDCarierModel.class);
        discussionDCarierModel.getDiscussionID().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                mDiscussionID = s;


                //display reviews if they exist in the dataBase
                try {

                    setupReviewsList(s);

                }catch (NullPointerException e){
                    Log.e(TAG, "onChanged: NullPointerException : " + e.getMessage() );
                }

            }
        });
    }

    private void updateReply(String mReply){

        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference userRepliesRef = myRef.child("institutions_courses").child(course.getInstitution_id())
                .child(course.getCourse_id())
                .child("discussions")
                .child(mDiscussionID);




        userRepliesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                Replies reply = new Replies();
                reply.setUsername(profileDetails.getUsername());
                reply.setProfile_pic(profileDetails.getProfile_photo());
                reply.setReply(mReply);
                reply.setDate_created(Methods.getTimeStamp());



                userRepliesRef.child("replies").setValue(reply);

                progressBar.setVisibility(View.GONE);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressBar.setVisibility(View.GONE);

            }
        });




    }

    private void  getUserDetails(){

        if (Methods.checkCurrentUser(mAuth.getCurrentUser())){

            /*Read Data-Base */
            myRef.child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //retrieve user data from the data-base
                    profileDetails = Methods.getUserProfileData(dataSnapshot , mAuth);


                    // set userProfile pic
                    if (isAdded()){
                        Glide
                                .with(getContext())
                                .load(profileDetails.getProfile_photo())
                                .placeholder(R.drawable.user_colored)
                                .into(currentUserProfile);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    private void setupReplyUpdate(){

        sendReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Methods.checkCurrentUser(mAuth.getCurrentUser())){

                    if (reply.length() != 0){
                        updateReply(reply.getText().toString());

                    }else{
                        Toast.makeText(getContext() , "Cannot add an empty reply..!" , Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Navigation.findNavController(view).navigate(R.id.login_flow_action);
                }

            }
        });

    }

    // get discussion id from 


}
