package com.example.infinity.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.infinity.R;

import java.io.File;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    /*widgets*/
    private View view ;
    private ImageView backButton ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search , container , false);
        initialize();

        Log.d(TAG, "onCreateView: the directory lebgth is ##################" + new File(getContext().getExternalFilesDir(null) ,"amajilanjilanwebemrhetjha").length());


        return view;

    }

    private void initialize(){
        /*toolbar*/
        backButton = view.findViewById(R.id.search_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });


    }

}
