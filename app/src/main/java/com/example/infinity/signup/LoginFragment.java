package com.example.infinity.signup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.infinity.R;
import com.example.infinity.Utils.Methods;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LoginFragment extends Fragment {

    /*vars*/
    private static final String TAG = "LoginFragment";
    private static final int RC_SIGN_IN = 9001;

    /*widgets*/
    private View view;
    private MaterialButton  loginBtn;
    private TextInputEditText loginEmail ,loginPassword ;
    private TextView forgotPwdBtn , googleSignInBtn ,facebookLoginBtn  ;
    private LinearLayout signUpBtn ;
    private ProgressBar loginProgressBar ;
    private ImageView backButton ;



    /*firebase*/
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_in , container ,false);

        /*initializing widgets*/
        initialize();

        /*configure google sign in*/
        configureGoogleSignIn();

        initLogin();

        /*when clicking on sign up*/
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.register_action);
            }
        });

        /*when clicking on forgot password btn*/
        forgotPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.reset_pwd_action);
            }
        });

        /*when user click on google sign in Txt Btn*/
        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        /*when user click on facebook sign in method*/
        facebookLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity() , FacebookLogin.class);
                startActivity(intent);

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });



        return view ;
    }

    /*initialize widgets*/
    private void initialize(){
        loginBtn = view.findViewById(R.id.sign_in_button);
        forgotPwdBtn =view.findViewById(R.id.forgot_pwd_txt);
        signUpBtn = view.findViewById(R.id.login_sign_up_container);
        loginProgressBar = view.findViewById(R.id.login_progress_bar);
        loginEmail = view.findViewById(R.id.login_email);
        loginPassword =view.findViewById(R.id.login_password);
        googleSignInBtn = view.findViewById(R.id.google_sign_in);
        facebookLoginBtn = view.findViewById(R.id.facebook_sign_in);
        backButton = view.findViewById(R.id.login_back_button);



    }

    /*initialize login*/
    private void initLogin(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference() ;

        /*initialize button for loging in*/
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForColorStateLists")
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: attempting to login");


                if (checkAllFields()){

                    String email = loginEmail.getText().toString();
                    String password = loginPassword.getText().toString();

                    loginProgressBar.setVisibility(View.VISIBLE);


                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        try {
                                            assert user != null;
                                            if (user.isEmailVerified()){

                                                Log.d(TAG, "signInWithEmail:success");
                                                Toast.makeText(getContext(), "logged in successfully.",
                                                        Toast.LENGTH_SHORT).show();
                                                loginProgressBar.setVisibility(View.GONE);
                                                requireActivity().onBackPressed();

                                            }else {
                                                Log.d(TAG, "signInWithEmail:failed");
                                                Toast.makeText(getContext(), "email is  not verified. check your email inbox",
                                                        Toast.LENGTH_LONG).show();
                                                loginProgressBar.setVisibility(View.GONE);
                                                mAuth.signOut();
                                            }

                                        }catch (NullPointerException e){
                                            Log.e(TAG, "onComplete: null pointer exception :" + e.getMessage() );
                                        }



                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.d(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(getContext(), "login failed. " + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();

                                        loginProgressBar.setVisibility(View.INVISIBLE);
                                    }


                                }
                            });

                }


            }
        });
    }

    /*Checking if all fields do not have errors*/
    private boolean checkAllFields(){

        if (loginEmail.length() == 0){
            loginEmail.setError("email required");
            return false ;
        }

        if (loginPassword.length() == 0){
            loginPassword.setError("password required");
            return false ;
        }

        return true ;
    }



    /*on activity result method*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN ) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }

        }

    }


    //################################################################### GOOGLE SIGN IN METHODS #############################################################

    /*method for configuring google sign client
     * Gmail account sign in*/
    private void configureGoogleSignIn(){
        // Configure Google Sign In client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

    }

    /*method for firebase auth with google
     * Gmail account sign in*/

    private void firebaseAuthWithGoogle(String idToken) {

        loginProgressBar.setVisibility(View.VISIBLE);

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) requireContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            /*user profile info*/
                            FirebaseUser user = mAuth.getCurrentUser();
                            String username =  Methods.condensingString(user.getDisplayName());
                            String userId = user.getUid();
                            String email = user.getEmail();
                            String fullName = user.getDisplayName();
                            Uri photoUrl = user.getPhotoUrl();
                            String profilePhoto = photoUrl.toString();

                            /*add new  user information to the database*/
                            Query query = myRef.child("users").orderByKey().equalTo(userId);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (!dataSnapshot.exists()){
                                        Methods.addUserToDataBase(myRef ,userId ,username ,email ,fullName , profilePhoto);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });




                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(getContext(), "logged in successfully.",
                                    Toast.LENGTH_SHORT).show();
                            loginProgressBar.setVisibility(View.GONE);
                            requireActivity().onBackPressed();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "Sign in with Google Account failed!. " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();

                            loginProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }





}
