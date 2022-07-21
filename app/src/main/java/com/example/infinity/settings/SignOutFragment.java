package com.example.infinity.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.infinity.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignOutFragment extends Fragment {

    private static final String TAG = "SignOutFragment";

    /*widgets*/
    private View view;
    private ProgressBar mProgressBar ;
    private TextView textView ;
    private MaterialButton mSignOutBtn;

    /*fire-base*/
    private FirebaseAuth mAuth ;
    private FirebaseAuth.AuthStateListener mAuthListener ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");

        view = inflater.inflate(R.layout.fragment_sign_out ,container ,false);

        /*initialize widgets in create view*/
        initialize();

        /*setup firebase auth changes*/
        setupFirebaseAuthListener();


        /*completely signing the user out*/
        mSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: attempting to sign out");

                mProgressBar.setVisibility(View.VISIBLE);

                /*sign out*/
                mAuth.signOut();


            }
        });


      return view ;
    }

    /*initialize widgets*/
    private void initialize(){
        textView = view.findViewById(R.id.text_view_confirm_sign_out);
        mProgressBar =view.findViewById(R.id.login_progress_bar);
        mSignOutBtn = view.findViewById(R.id.btn_confirm_sign_out);

    }

    //*setup firebase auth listener*//*
    private void setupFirebaseAuthListener(){
        Log.d(TAG, "setupFirebaseAuthListener: setting-up firebase-Auth");
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){

                    Log.d(TAG, "onAuthStateChanged: signed-in " + user.getUid() );

                } else{
                    Log.d(TAG, "onAuthStateChanged: signed-out " );

                    mProgressBar.setVisibility(View.GONE);

                    Log.d(TAG, "onAuthStateChanged: navigating to home page");
                    Navigation.findNavController(view).navigate(R.id.pop_out_of_account_settings_graph_action);


                };

            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mAuth.removeAuthStateListener(mAuthListener);
    }
}
