package com.example.infinity.Utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "TabsPagerAdapter";
    private List<Fragment> fragmentList = new ArrayList<>();

    public TabsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /*add fragments method*/
    public void addFragments (Fragment fragment){
        fragmentList.add(fragment);
    }
}
