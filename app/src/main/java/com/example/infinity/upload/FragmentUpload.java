package com.example.infinity.upload;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.infinity.Models.device.getPermissions;
import com.example.infinity.R;
import com.example.infinity.Utils.Methods;
import com.example.infinity.Utils.TabsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class FragmentUpload extends Fragment {
    private static final String TAG = "FragmentUpload";
    private static final int VERIFY_PERMISSIONS_INT = 23 ;

    /*Widgets*/
    private View view ;
    private ViewPager containerViewpager;
    private TabLayout tabLayout;

    /*Adapters*/
    private TabsPagerAdapter pagerAdapter;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload  , container ,false);

        /*initialize widgets*/
        initialize();
        setUpViewPager();





        return view;
    }


    private void initialize(){
        containerViewpager = view.findViewById(R.id.main_container);
        tabLayout = view.findViewById(R.id.upload_photo_main_tabs);

    }


    private void setUpViewPager(){

        pagerAdapter = new TabsPagerAdapter(getChildFragmentManager() , FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragments(new GalleryFragment());
        pagerAdapter.addFragments(new CameraFragment());

        containerViewpager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(containerViewpager);
        tabLayout.getTabAt(0).setText("Gallery");
        tabLayout.getTabAt(1).setText("Camera");
    }
}
