package com.ducanh.appchat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ducanh.appchat.R;
import com.ducanh.appchat.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ChatHomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View view;
    private ViewPagerAdapter adapter;

    public ChatHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_chat_home, container, false);

        tabLayout= view.findViewById(R.id.tab_layout);
        viewPager= view.findViewById(R.id.view_pager);
        adapter= new ViewPagerAdapter(getChildFragmentManager(),
                ViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_chat_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_supervised_user_circle_24);
        return view;
    }
}