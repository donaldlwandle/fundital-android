package com.example.infinity.profile;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.infinity.Models.database.UserData;
import com.example.infinity.Models.database.UserProfileData;
import com.example.infinity.Models.database.courseinfo.CourseSkills;
import com.example.infinity.R;
import com.example.infinity.Utils.CourseInfoAdapters;
import com.example.infinity.Utils.Methods;
import com.example.infinity.Utils.TabsPagerAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    /*Widgets*/
    private View profileView ;
    private ImageView optionsButton , profileBackButton ;
    private TextView editProfileBtn , displayName , displayProfession ;
    private RecyclerView interestsContainer ;
    private CircleImageView profileImage ;
    private LinearLayout profileContentContainer ;
    private ShimmerFrameLayout profileShimmerLayout ;

    /*firebase*/
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase ;
    private DatabaseReference myRef ;

    /*vars*/
    private List<CourseSkills> courseSkillsList ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ProfileFragment Started");
        profileView = inflater.inflate(R.layout.fragment_profile , container , false);

        /*initialize widgets*/
        initialize();








        /*when clicking on options button*/
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.account_settings_graph_action);
            }
        });

        /*navigating to edit Profile Button*/
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Methods.checkCurrentUser(mAuth.getCurrentUser())){
                    Navigation.findNavController(view).navigate(R.id.edit_profile_action);

                }else{
                    Navigation.findNavController(view).navigate(R.id.login_flow_action);
                }

            }
        });



        return  profileView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    private void  initialize(){
        optionsButton = profileView.findViewById(R.id.profile_options_button);
        editProfileBtn = profileView.findViewById(R.id.edit_profile_btn);
        profileBackButton = profileView.findViewById(R.id.profile_back_button);
        interestsContainer = profileView.findViewById(R.id.interests_recycler);
        profileImage = profileView.findViewById(R.id.profile_profile_pic);
        displayName = profileView.findViewById(R.id.profile_username);
        displayProfession = profileView.findViewById(R.id.profile_profession);
        profileShimmerLayout = profileView.findViewById(R.id.profile_shimmer_layout);
        profileContentContainer = profileView.findViewById(R.id.profile_content_view);

        profileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });

        /*initialise firebase vars*/
        mAuth = FirebaseAuth.getInstance() ;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        profileShimmerLayout.startShimmer();


        init();

    }

    private void init(){



        /*Read Data-Base */
        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {

                    //retrieve user data from the data-base
                    populateProfileWidgets(Methods.getUserProfileData(dataSnapshot , mAuth));


                    // retrieve user interests
                    /*setup user  interests*/
                    setupInterestsContainer();

                }catch (NullPointerException e){
                    Log.e(TAG, "onCreateView: NullPointerException : " + e.getMessage()  );
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    /**
     * method for setting user interests recycler*
     * the same adapter used for setting course skills ia used to setup user interests
     * */
    private void setupInterestsContainer(){
        courseSkillsList = new ArrayList<>();
        CourseSkills skills1 = new CourseSkills();
        skills1.SKILL = "Mathematics";

        CourseSkills skills2 = new CourseSkills();
        skills2.SKILL = "Finance";

        CourseSkills skills3 = new CourseSkills();
        skills3.SKILL = "Beauty and Health";

        CourseSkills skills4 = new CourseSkills();
        skills4.SKILL = "philosophy";

        courseSkillsList.add(skills1);
        courseSkillsList.add(skills2);
        courseSkillsList.add(skills3);
        courseSkillsList.add(skills4);
        courseSkillsList.add(skills2);

        /*Adapters*/
        CourseInfoAdapters.CourseSkillsAdapter interestsAdapter = new CourseInfoAdapters.CourseSkillsAdapter(getContext(), courseSkillsList);

        /*setup layout manager*/
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);

        interestsContainer.setLayoutManager(layoutManager);


        /*set adapter*/
        interestsContainer.setAdapter(interestsAdapter);



    }




    private void populateProfileWidgets(UserData userProfileData){



        /*Setting profile photo if it exist*/

        Glide
                .with(getContext())
                .load(userProfileData.getProfile_photo())
                .placeholder(R.drawable.user_colored)
                .into(profileImage);

        /*setting username*/
        displayName.setText("@" + userProfileData.getUsername());

        /*setting PROFESSION*/
        displayProfession.setText(userProfileData.getProfession());

        profileShimmerLayout.stopShimmer();
        profileShimmerLayout.setShimmer(null);
        profileContentContainer.setVisibility(View.VISIBLE);
        profileShimmerLayout.setVisibility(View.GONE);

    }

}