package com.example.infinity.profile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.infinity.Models.database.UserData;
import com.example.infinity.Models.database.UserPrivateData;
import com.example.infinity.Models.database.UserProfileData;
import com.example.infinity.Models.device.getPermissions;
import com.example.infinity.R;
import com.example.infinity.Utils.Methods;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "FragmentEditProfile";
    private static final int VERIFY_PERMISSIONS_INT = 313;

    //Widgets
    private View view ;
    private TextView saveProfile ;
    private ImageView editProfileBackButton ;
    private CircleImageView profileImage ;
    private RelativeLayout editProfile;
    private TextInputLayout usernameIPL , dateOfBirthIPL , genderIPL ,professionIPL ;
    private TextInputEditText usernameEDT , dateOfBirthEDT , genderEDT  , professionEDT;

    /*firebase*/
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase ;
    private DatabaseReference myRef ;

    /*vars*/
    private String userID , username ,dateOfBirth , gender ,profession ,DBgender;
    private  UserData mUserProfile;
    private UserPrivateData mUserPrivateData ;
    private volatile boolean FLAG =  true;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile , container , false) ;


        /*initialize widgets*/
        initialize();

        /*populating profile info */
        UpdateProfileInfo();




        /*updating the profile info to the database*/
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfileInfo();
            }
        });



        /*updating date of birth for the first time*/
        dateOfBirthEDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        /*selecting gender*/
        genderEDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Methods.genderAlertDialog(genderEDT , getContext());
            }
        });


        /*edit profile image*/
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*checking if permissions are verified on launch*/
                if (!Methods.checkingPermissions(getPermissions.PERMISSIONS , getContext())){
                    Methods.verifyPermissions(getPermissions.PERMISSIONS , getActivity() ,VERIFY_PERMISSIONS_INT);
                }else{
                    Navigation.findNavController(view).navigate(R.id.upload_image_action);
                }


            }
        });

        return  view;
    }



    /*initialize widgets*/
    private void initialize(){
        saveProfile = view.findViewById(R.id.save_profile_button);
        editProfileBackButton = view.findViewById(R.id.edit_profile_back_button);
        editProfile =view.findViewById(R.id.edit_profile_container);
        usernameIPL = view.findViewById(R.id.username_ipl);
        dateOfBirthIPL = view.findViewById(R.id.date_of_birth_ipl);
        genderIPL = view.findViewById(R.id.gender_ipl);
        usernameEDT = view.findViewById(R.id.username_txt);
        dateOfBirthEDT = view.findViewById(R.id.date_of_birth_txt);
        genderEDT = view.findViewById(R.id.gender_txt);
        professionEDT = view.findViewById(R.id.profession_txt);
        professionIPL = view.findViewById(R.id.profession_ipl);
        profileImage = view.findViewById(R.id.profile_profile_pic);

        editProfileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });



        /*initialise firebase vars*/
        mAuth = FirebaseAuth.getInstance() ;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getUid();
    }


    /*
    * method for showing the date picker dialog
    * will be called when users set their date of birth
    * will only be called once  in a first edit profile instance
    * */
    private void showDatePickerDialog(){


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext() ,
                this ,
                Calendar.getInstance().get(Calendar.YEAR) ,
                Calendar.getInstance().get(Calendar.MONTH) ,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.YEAR , -75);

        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.YEAR , -10);


        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        datePickerDialog.show();

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

        String DOB = dayOfMonth + "/" + month + "/" +year ;
        dateOfBirthEDT.setText(DOB);

    }

    /*method for reading profile data and populating the data*/
    private void UpdateProfileInfo(){
        Log.d(TAG, "SetupProfilePage: attempting to set-up the user profile data");


        /*Read Data-Base */
        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //retrieve user data from the data-base
                populateProfileWidgets(Methods.getUserProfileData(dataSnapshot , mAuth));



                // retrieve user interests

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*Read Data-Base */
        myRef.child("users_private_data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //retrieve user data from the data-base
                populateProfilePrivateWidgets(Methods.getUserPrivateData(dataSnapshot , mAuth));



                // retrieve user interests

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void populateProfileWidgets( UserData userProfileData){

        mUserProfile = userProfileData;

        /*Setting profile photo if it exist*/
        if (isAdded()){
            Glide
                    .with(this)
                    .load(mUserProfile.getProfile_photo())
                    .placeholder(R.drawable.user_colored)
                    .into(profileImage);
        }


        /*setting username*/
        if (mUserProfile.getUsername() != null ){
            usernameEDT.setText(mUserProfile.getUsername());
        }



        /*setting PROFESSION*/
        if (mUserProfile.getProfession() != null  ){
            professionEDT.setText(mUserProfile.getProfession());
        }



    }


    private void populateProfilePrivateWidgets( UserPrivateData userProfileData){

        mUserPrivateData = userProfileData;




        /*
         * hide visibility of gender and birthday widgets if gender exist in the database
         * un-hide visibility of gender and birthday widgets if gender does not exist in the database
         * */


        if (mUserPrivateData.getGender().length() == 0){
            genderIPL.setVisibility(View.VISIBLE);
            dateOfBirthIPL.setVisibility(View.VISIBLE);

        }else {
            genderIPL.setVisibility(View.GONE);
            dateOfBirthIPL.setVisibility(View.GONE);
        }


    }


    private void saveProfileInfo(){

        /*get data*/

        if(usernameEDT.getText().toString().equals("")){
            username = mUserProfile.getUsername();
        }else {
            username = usernameEDT.getText().toString() ;
        }

        if (professionEDT.getText().toString().equals("")){
            profession = mUserProfile.getProfession() ;
        }else{
            profession = professionEDT.getText().toString();
        }

        dateOfBirth = dateOfBirthEDT.getText().toString();
        gender = genderEDT.getText().toString();


        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                /*check if user changed username or not*/
                if (!mUserProfile.getUsername().equals(username)){
                    // user changed  username
                    CheckIfUserNameExist(username);


                }

                /*check if user made changes to their profession*/
                if (!mUserProfile.getProfession().equals(profession)){
                    // user changed  username
                    Methods.updateProfileDetails(profession ,null ,null ,userID , null , myRef);
                    Toast.makeText(getContext() , "Changes Updated" , Toast.LENGTH_SHORT).show();

                }

                /*update gender and  birth date if they have never been set*/
                if (mUserPrivateData.getGender().length() == 0){
                    /*user does'nt have date of birth and gender on database
                    * we therefore add them to private data*/

                    if (checkAllFields()){
                        /*save user date of birth and gender*/
                        Methods.updateProfileDetails(null ,dateOfBirth , gender ,userID ,null ,myRef);

                        //user have updated their date of birth and gender
                        Toast.makeText(getContext() , "Changes Updated" , Toast.LENGTH_SHORT).show();
                    }



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    /*
    * check if the username already exist in the database
    * */
    private void CheckIfUserNameExist(final String mUsername) {
        Log.d(TAG, "CheckIfUserNameExist: checking if " + mUsername + " exist in the database");


        Query query = myRef.child("users")
                .orderByChild("username")
                .equalTo(mUsername);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()){
                    // add the username
                    Methods.updateUsername(username , userID ,myRef);
                    Toast.makeText(getContext() , "Changes Saved" , Toast.LENGTH_LONG).show();
                }

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                    if (singleSnapshot.exists()){
                        Log.d(TAG, "onDataChange: FOUND A MATCH " + singleSnapshot.getValue(mUserProfile.getClass()).getUsername());
                        Toast.makeText(getContext() , "That username is already taken" , Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /*Checking if all fields do not have errors*/
    private boolean checkAllFields(){

        if (dateOfBirthEDT.length() == 0){
            dateOfBirthEDT.setError("email required");
            return false ;
        }

        if (genderEDT.length() == 0){
            genderEDT.setError("password required");
            return false ;
        }




        return true ;
    }



    


}
