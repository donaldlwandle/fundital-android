package com.example.infinity.discussion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.infinity.CourseGetterViewModel;
import com.example.infinity.Models.database.Courses;
import com.example.infinity.Models.database.Discussions;
import com.example.infinity.Models.database.UserData;
import com.example.infinity.R;
import com.example.infinity.Utils.Methods;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddNewDiscussionFragment extends Fragment {
    private static final String TAG = "AddNewDiscussionFragmen";

    /*widgets*/
    private View view;
    private ImageView backButton ;
    private TextView postButton ;
    private MaterialButton mUploadButton;
    private EditText  discussionText;
    private ProgressBar progressBar;

    /*firebase*/
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase ;
    private DatabaseReference myRef ;

    /*Vars*/
    private String userID;
    private UserData userData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_add_discussion ,container , false);
        initialize();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCourseIDsFromParent();

    }



    private void initialize(){
        backButton = view.findViewById(R.id.add_discussion_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });

        postButton = view.findViewById(R.id.upload_post_button);
        mUploadButton = view.findViewById(R.id.post_button);
        discussionText = view.findViewById(R.id.discussion_edt);
        progressBar = view.findViewById(R.id.login_progress_bar);


        /*initialise firebase vars*/
        mAuth = FirebaseAuth.getInstance() ;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getUid();
    }

    /*Getting course ID's From LiveData View Model updated when navigating to the current course
     * View Model is CourseGetterViewModel*/
    private void getCourseIDsFromParent(){
        /*View Models*/
        CourseGetterViewModel courseGetterViewModel = new ViewModelProvider(requireActivity()).get(CourseGetterViewModel.class);
        courseGetterViewModel.getCourse().observe(getViewLifecycleOwner(), new Observer<Courses>() {
            @Override
            public void onChanged(Courses courses) {
                getUserInformation(courses);
            }
        });
    }

    /*Get username , userProfile pic*/
    private void getUserInformation(Courses mCourse) {
        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userData = Methods.getUserProfileData(dataSnapshot ,mAuth);

                postButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadNewDiscussion(mCourse);
                    }
                });

                mUploadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadNewDiscussion(mCourse);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadNewDiscussion(Courses courseInfo){


        if (discussionText.length() == 0){
            Toast.makeText(getContext() , "cannot post an empty discussion!",Toast.LENGTH_SHORT).show();
        }else{

            progressBar.setVisibility(View.VISIBLE);

            String newDiscussionId = myRef.child("institutions_courses").
                    child(courseInfo.getInstitution_id()).child(courseInfo.getCourse_id()).
                    push().getKey().toString();

            Discussions newDiscussion = new Discussions();
            newDiscussion.setUser_id(userID);
            newDiscussion.setDiscussion_id(newDiscussionId);
            newDiscussion.setUsername(userData.getUsername());
            newDiscussion.setInstructor("no");
            newDiscussion.setProfile_pic(userData.getProfile_photo());
            newDiscussion.setDiscussion(discussionText.getText().toString());
            newDiscussion.setDate_created(Methods.getTimeStamp());

            myRef.child("institutions_courses").
                    child(courseInfo.getInstitution_id()).child(courseInfo.getCourse_id())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    myRef.child("institutions_courses").
                            child(courseInfo.getInstitution_id()).child(courseInfo.getCourse_id()).
                            child("discussions").child(newDiscussionId).
                            setValue(newDiscussion);

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Discussion Submitted Successfully" ,Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"The following error occurred: /n"
                            +databaseError.getMessage() + "/n  PLEASE TRY AGAIN OR TRY LATER If THIS CONTINUES TO HAPPEN",
                            Toast.LENGTH_SHORT).show();
                }
            });


        }
    }


}
