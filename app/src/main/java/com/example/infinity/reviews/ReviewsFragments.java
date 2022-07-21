package com.example.infinity.reviews;

import android.app.Dialog;
import android.app.DownloadManager;
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
import com.example.infinity.Models.database.Courses;
import com.example.infinity.Models.database.Reviews;
import com.example.infinity.Models.database.UserData;
import com.example.infinity.R;
import com.example.infinity.Utils.Methods;
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


public class ReviewsFragments extends Fragment {

    private static final String TAG = "ReviewsFragments";

    /*widgets*/
    private View view ;
    private ImageView mBackButton ;
    private RecyclerView mRecyclerView ;
    private Dialog rateDialog ;
    private ProgressBar progressBar ;
    private CircleImageView currentUserProfile , sendReviewButton ;
    private EditText review ;


    /*firebase*/
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase ;
    private DatabaseReference myRef ;

    //vars
    private Courses course;
    private UserData profileDetails ;


    /*Adapter*/
    private ReviewsAdapter adapter ;

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

        mBackButton = view.findViewById(R.id.reviews_back_button);
        mRecyclerView = view.findViewById(R.id.reviews_list_view);
        progressBar = view.findViewById(R.id.login_progress_bar);
        currentUserProfile = view.findViewById(R.id.reviews_user_profile_pic);
        review = view.findViewById(R.id.review);
        sendReviewButton = view.findViewById(R.id.send_review_button);

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
    private void setupReviewsList(){

        DatabaseReference reviewsQuery = myRef.child("institutions_courses")
                .child(course.getInstitution_id())
                .child(course.getCourse_id())
                .child("reviews");

        reviewsQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Reviews> list = new ArrayList<>();

                        for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()){
                            list.add(singleSnapShot.getValue(Reviews.class));
                        }

                        /*setup reviews recycler view layout manager*/
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        mRecyclerView.setHasFixedSize(true);

                        /*set adapter to recyclerView*/
                        adapter = new ReviewsAdapter(list , getContext());
                        mRecyclerView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }


    /*Setup Rate Dialog*/
    private void setUpRateDialog(){
        rateDialog = new Dialog(requireActivity());
        rateDialog.setCancelable(true);
        Objects.requireNonNull(rateDialog.getWindow()).getAttributes().windowAnimations = R.style.dialogAnimation;
        rateDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);

        rateDialog.setCanceledOnTouchOutside(false);

        View view  = requireActivity().getLayoutInflater().inflate(R.layout.custom_dialog, null);
        rateDialog.setContentView(view);

        NavController navController = (NavController) Navigation.findNavController(requireActivity() ,R.id.root_nav_host_fragment);

        TextView dismiss = (TextView) view.findViewById(R.id.btn_dismiss);
        TextView submit = (TextView) view.findViewById(R.id.btn_submit) ;
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.get_ratting_bar);



        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateDialog.dismiss();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Methods.checkCurrentUser(mAuth.getCurrentUser())){

                    updateReview(null , String.valueOf(ratingBar.getRating()));
                    rateDialog.dismiss();


                }else {
                    rateDialog.dismiss();
                    navController.navigate(R.id.login_flow_action);
                }



            }
        });



        rateDialog.show();
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

                //display reviews if they exist in the dataBase
                try {

                    setupReviewsList();

                }catch (NullPointerException e){
                    Log.e(TAG, "onChanged: NullPointerException : " + e.getMessage() );
                }

                //setup rate dialog
                setUpRateDialog();

                setupReviewUpdate();
            }
        });
    }




    private void updateReview(String mReview , String mRating){

        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference userReviewsRef = myRef.child("institutions_courses").child(course.getInstitution_id())
                .child(course.getCourse_id());



        userReviewsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                if (mReview != null){

                    DataSnapshot ratingSnapShot = dataSnapshot.child("reviews").child(mAuth.getUid()).child("rating");

                    Reviews reviewsReview = new Reviews();
                    reviewsReview.setUsername(profileDetails.getUsername());
                    reviewsReview.setProfile_pic(profileDetails.getProfile_photo());
                    reviewsReview.setReview(mReview);
                    reviewsReview.setDate_created(Methods.getTimeStamp());

                    if (ratingSnapShot.exists()){
                        reviewsReview.setRating(ratingSnapShot.getValue().toString());
                    }


                    userReviewsRef.child("reviews")
                            .child(mAuth.getUid())
                            .setValue(reviewsReview);

                    progressBar.setVisibility(View.GONE);
                }

                if (mRating != null){

                    DataSnapshot reviewSnapShot = dataSnapshot.child("reviews").child(mAuth.getUid()).child("review");

                    Reviews reviewsRating = new Reviews();
                    reviewsRating.setUsername(profileDetails.getUsername());
                    reviewsRating.setProfile_pic(profileDetails.getProfile_photo());
                    reviewsRating.setRating(mRating);
                    reviewsRating.setDate_created(Methods.getTimeStamp());

                    if (reviewSnapShot.exists()){
                        reviewsRating.setReview(reviewSnapShot.getValue().toString());
                    }

                    userReviewsRef.child("reviews")
                            .child(mAuth.getUid())
                            .setValue(reviewsRating);

                    progressBar.setVisibility(View.GONE);

                }



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

    private void setupReviewUpdate(){

        sendReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Methods.checkCurrentUser(mAuth.getCurrentUser())){

                    if (review.length() != 0){
                        updateReview(review.getText().toString() , null);

                    }else{
                        Toast.makeText(getContext() , "Cannot add an empty review..!" , Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Navigation.findNavController(view).navigate(R.id.login_flow_action);
                }

            }
        });

    }


}
