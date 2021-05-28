package com.ducanh.appchat.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ducanh.appchat.fragments.ChatHomeFragment;
import com.ducanh.appchat.fragments.CreatorFragment;
import com.ducanh.appchat.fragments.HomeFragment;

public class FargmentNavigationAdapter extends FragmentStatePagerAdapter {
    private int numPage=3;
    public FargmentNavigationAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new HomeFragment();
            case 1: return new CreatorFragment();
            case 2: return new ChatHomeFragment();
            default: return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return numPage;
    }
}
