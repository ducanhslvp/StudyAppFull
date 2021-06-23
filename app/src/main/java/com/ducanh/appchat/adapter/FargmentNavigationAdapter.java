package com.ducanh.appchat.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ducanh.appchat.fragments.ChatHomeFragment;
import com.ducanh.appchat.fragments.ClassFragment;
import com.ducanh.appchat.fragments.StudyFragment;
import com.ducanh.appchat.fragments.HomeFragment;

public class FargmentNavigationAdapter extends FragmentStatePagerAdapter {
    private int numPage=4;
    public FargmentNavigationAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new HomeFragment();
            case 1: return new StudyFragment();
            case 2: return new ClassFragment();
            case 3: return new ChatHomeFragment();
            default: return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return numPage;
    }
}
