package com.example.infinity.signup;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.infinity.Models.database.UserData;
import com.example.infinity.Models.database.UserPrivateData;
import com.example.infinity.R;
import com.example.infinity.Utils.Methods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";

    /*widgets*/
    private View view ;
    private MaterialButton signUpButton ;
    private TextInputEditText registerEmail ,registerPassword ,registerFullName , confirmPassword ;
    private ProgressBar progressBar ;
    private ImageView backButton ;

    /*firebase*/
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    /*vars*/
    private  String userID , email ,fullName ,password ,passwordConfirm  ;
    private String append = "";
    private String username = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register , container ,false);

        /*initialize widgets*/
        initialize();

        /*sign up*/
        initRegister();
        return  view ;
    }

    /*initialize registering a new user*/
    private void initRegister(){

        mAuth = FirebaseAuth.getInstance();
        mDatabase =FirebaseDatabase.getInstance();
        myRef =  mDatabase.getReference();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.d(TAG, "onClick: Attempting to Register a new user");

                if (checkAllFields()){

                    email = registerEmail.getText().toString();
                    fullName = registerFullName.getText().toString();
                    password = registerPassword.getText().toString();
                    passwordConfirm = confirmPassword.getText().toString();

                    if (!password.equalsIgnoreCase(passwordConfirm)){

                        Toast.makeText(getContext() , "Passwords do not Match!" ,Toast.LENGTH_LONG).show();

                    }else{

                        /*register a new user with email and password*/
                        registerNewUser(email , password);

                    }


                }


            }
        });


    }

    /*initialize widgets*/
    private void initialize(){
        signUpButton = view.findViewById(R.id.register_button);
        registerEmail = view.findViewById(R.id.register_email);
        registerPassword = view.findViewById(R.id.register_password);
        registerFullName = view.findViewById(R.id.register_full_name);
        confirmPassword = view.findViewById(R.id.register_confirm_password);
        progressBar = view.findViewById(R.id.login_progress_bar);
        backButton = view.findViewById(R.id.register_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

    }


    /*Checking if all fields do not have errors*/
    private boolean checkAllFields(){

        if (registerEmail.length() == 0){
            registerEmail.setError("email required");
            return false ;
        }

        if (registerPassword.length() == 0){
            registerPassword.setError("password required");
            return false ;
        }

        if (registerFullName.length() == 0){
            registerFullName.setError("full name is required!");
            return false ;
        }

        if (confirmPassword.length() == 0){
            confirmPassword.setError("password required!");
            return  false ;
        }


        return true ;
    }

    /*Register new user with email and password*/
    private  void registerNewUser(final String email , String password){

        progressBar.setVisibility(View.VISIBLE);


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "createUserWithEmail:success" + userID);

                            //Upload user Account data to the database
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    CheckIfUserNameExist(fullName);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            Toast.makeText(getContext(), "Account created. Verification email has been sent to " + email + ".",
                                    Toast.LENGTH_LONG).show();

                            /*send verification email*/
                            sendVerificationEmail(mAuth.getCurrentUser());


                            requireActivity().onBackPressed();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Creating Account failed." + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();

                        }
                        progressBar.setVisibility(View.GONE);

                        // ...
                    }
                });

    }

    /*method for checking if the username exist in the data base
    * return true or false
    * */

    private void CheckIfUserNameExist(final String mUsername) {
        Log.d(TAG, "CheckIfUserNameExist: checking if " + mUsername + " exist in the database");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("users")
                .orderByChild("username")
                .equalTo(mUsername);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                    if (singleSnapshot.exists()){
                        Log.d(TAG, "onDataChange: FOUND A MATCH " + singleSnapshot.getValue(UserData.class).getUsername());
                        append = myRef.push().getKey().substring(3 , 10);
                        username = fullName + append ;

                    }else{

                        username = Methods.condensingString(fullName);
                    }

                }


                /*Add new user to the database */
                Methods.addUserToDataBase(myRef,userID , username , email , fullName , "");

                mAuth.signOut();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    /*
    * method for sending verification email
    * if user is registered successfully
    *
    * */

    private void sendVerificationEmail(FirebaseUser user){

        if (user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (!task.isSuccessful()){
                        Toast.makeText(getContext() , "could not send verification email : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

    }
}
