package com.example.infinity.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.infinity.R;
import com.google.android.material.button.MaterialButton;

public class ResetPasswordFragment extends Fragment {

    private static final String TAG = "ResetPasswordFragment";
    
    /*widgets*/
    private View view ;
    private MaterialButton resetPwdBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reset_password , container , false);

        /*initialize widgets*/
        initialize();

        /*reset button*/
        resetPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });
        
        
        return view ;
    }

    /*initialize widgets*/
    private void initialize(){
        resetPwdBtn = view.findViewById(R.id.reset_password_button);
    }

}
