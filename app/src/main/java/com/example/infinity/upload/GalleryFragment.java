package com.example.infinity.upload;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.infinity.Models.device.getFilePaths;
import com.example.infinity.R;
import com.example.infinity.VideoPlayerViewModel;
import com.example.infinity.Utils.GridImageAdapter;
import com.example.infinity.Utils.Methods;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment  implements GridImageAdapter.OnItemClickedListener {
    /* Constants */
    private static final String TAG = "GalleryFragment";
    private static final int GALLERY_REQ_CODE = 12 ;

    /*Widgets*/
    private View view ;
    private ImageView cancelUploadButton , galleryImage ;
    private TextView nextButton ;
    private Spinner directorySpinner;
    private ProgressBar progressBar ;
    private RecyclerView imageGridView ;

    /*Vars*/
    private ArrayList<String> folders , imageUrls;
    private String mSelectedImg ;

    /*View Models*/
    private VideoPlayerViewModel imageSharedViewModel;

    /*firebase*/
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private StorageReference mStorageRef ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery , container ,false);
        /* initialize widgets */
        initialize();

        /*initialize image uploading*/
        init();

        /*cancel gallery image uploading uploading*/
        cancelUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: cancel gallery image upload");
                requireActivity().onBackPressed();
            }
        });
        
        /*navigating to next activity*/
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Methods.uploadUsersProfilePic(getActivity() ,mStorageRef ,myRef ,mAuth.getCurrentUser().getUid(), mSelectedImg);
            }
        });


        return  view;
    }

    private void initialize(){
        cancelUploadButton = view.findViewById(R.id.upload_photo_cancel_button);
        galleryImage = view.findViewById(R.id.upload_photo_gallery_image);
        nextButton = view.findViewById(R.id.upload_photo_next_button);
        directorySpinner = view.findViewById(R.id.spinner_directory);
        progressBar = view.findViewById(R.id.upload_photo_progress_bar);
        imageGridView = view.findViewById(R.id.upload_photo_gallery_grid_view);
    }

    private void  init(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference() ;
        mStorageRef = FirebaseStorage.getInstance().getReference();

        folders = new ArrayList<>();


        folders = Methods.getFolders(getFilePaths.PICTURES);
        folders.add(getFilePaths.CAMERA);
        folders.add("thhtj/Gallery");

        ArrayList<String> foldersNames = new ArrayList<>();
        for (int i = 0 ; i < folders.size() ; i ++){

            int index = folders.get(i).lastIndexOf("/");
            String string = folders.get(i).substring(index).replace("/" ,"");
            foldersNames.add(string);

        }

        /*setting up an array adapter for the spinner*/
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext() ,android.R.layout.simple_spinner_item ,foldersNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(spinnerAdapter);


        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position == folders.size() - 1){
                    pickImageFromGallery();

                }else {

                    setUpGridView(folders.get(position));

                }
            }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }

    /*this method will setup the image  grid view*/
    private void setUpGridView(String selectedFolder) {

        imageUrls = Methods.getFiles(selectedFolder);

        /*setup layoutManager*/
        GridLayoutManager layoutManager = new GridLayoutManager(getContext() ,3 ,GridLayoutManager.VERTICAL , false);
        imageGridView.setLayoutManager(layoutManager);

        /*adapt images to grid view using grid adapter*/
        GridImageAdapter imageAdapter = new GridImageAdapter(getContext() ,imageUrls , this);
        imageGridView.setAdapter(imageAdapter);

        /*display the first image in the list*/
        try {

            displayGalleryImage(imageUrls.get(0) , galleryImage);
            mSelectedImg = imageUrls.get(0);

        }catch (IndexOutOfBoundsException e){
            Log.e(TAG, "setUpGridView: IndexOutOfBoundsException" + e.getMessage());
        }



    }


    /*Method for displaying the Gallery Image*/
    private void displayGalleryImage(String imageUrl, ImageView galleryImage) {
        progressBar.setVisibility(View.VISIBLE);

        Glide
                .with(requireContext())
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(galleryImage);

    }

    /*this method will pick image from gallery*/

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        File filePictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Uri data = Uri.parse(filePictureDir.getPath());
        String type = "image/*";
        intent.setDataAndType(data ,type);
        startActivityForResult(intent ,GALLERY_REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            if (requestCode == GALLERY_REQ_CODE){

                Log.d(TAG, "onActivityResult: ready to deal with image selected");

                /*ready to deal with the selected image */

            }

        }
    }

    /*image clicked interface method*/
    @Override
    public void onItemClicked(int position) {

        displayGalleryImage(imageUrls.get(position) , galleryImage);
        mSelectedImg = imageUrls.get(position);

    }
}
