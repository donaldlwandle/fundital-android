package com.example.infinity.signup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infinity.MainActivity;
import com.example.infinity.R;
import com.example.infinity.Utils.Methods;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.concurrent.Executor;

public class FacebookLogin extends AppCompatActivity {
    private static final String TAG = "FacebookLogin";

    private CallbackManager mCallbackManager ;
    private ProgressBar loginProgressBar ;

    /*firebase*/
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        loginProgressBar = findViewById(R.id.login_progress_bar);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference() ;

        initFacebookLogin();

    }

    /*on activity result method*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        // Pass the activity result back to the Facebook SDK


    }


    //######################################################################### FACEBOOK SIGN IN METHODS ###########################################################################
    /**
     * Method for initializing facebook sign in button
     * This Method will be called when facebook sign in button is initialized
     * We are also configuring login manager
     * */
    private void initFacebookLogin(){

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this , Arrays.asList("email" , "public_profile"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });


    }

    /*method for handling facebook aces token from the user*/

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        loginProgressBar.setVisibility(View.VISIBLE);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
                            Toast.makeText(FacebookLogin.this, "logged in successfully.",
                                    Toast.LENGTH_SHORT).show();
                            loginProgressBar.setVisibility(View.GONE);

                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(FacebookLogin.this, "Sign in with Facebook Account failed!. " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();

                            loginProgressBar.setVisibility(View.INVISIBLE);

                        }

                        Intent intent = new Intent(FacebookLogin.this , MainActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
