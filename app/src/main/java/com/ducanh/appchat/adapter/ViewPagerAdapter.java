package com.ducanh.appchat.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ducanh.appchat.fragments.ChatsFragment;
import com.ducanh.appchat.fragments.UsersFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private int numPage=2;
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new ChatsFragment();
            case 1: return new UsersFragment();
            default: return new ChatsFragment();
        }
    }

    @Override
    public int getCount() {
        return numPage;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:return "CHAT";
            case 1:return "USER";
            default:return "CHAT";
        }
    }
}