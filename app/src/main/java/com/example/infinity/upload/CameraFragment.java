package com.example.infinity.upload;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.infinity.Models.device.getPermissions;
import com.example.infinity.R;
import com.example.infinity.Utils.Methods;
import com.google.android.material.button.MaterialButton;

import static android.app.Activity.RESULT_OK;

public class CameraFragment extends Fragment {

    /*constants*/
    private static final String TAG = "CameraFragment";
    private static final int CAMERA_REQ_CODE = 23541;

    /*Widgets*/
    private View view ;
    private MaterialButton cameraButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Camera fragment initialized");
        view = inflater.inflate(R.layout.fragment_camera , container ,false);
        /*initialising widgets*/
        initialize();

        /*opening the camera*/
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Methods.checkPermission(getPermissions.PERMISSIONS[2] , getContext())){
                    Log.d(TAG, "onClick: starting the camera");

                    /*start camera*/
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent , CAMERA_REQ_CODE);

                }else {

                    requireActivity().onBackPressed();

                }

            }
        });


        return view;
    }

    /* method for initializing widgets*/
    private void  initialize(){
        cameraButton = view.findViewById(R.id.open_camera_button);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            /*for camera result*/
            if (requestCode == CAMERA_REQ_CODE){
                Log.d(TAG, "onActivityResult: done taking photo");
                Log.d(TAG, "onActivityResult: ready to transfer data to the next screen");
            }

        }
    }
}
