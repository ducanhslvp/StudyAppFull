package com.ducanh.appchat.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ducanh.appchat.R;
import com.ducanh.appchat.TranslateActivity;

public class HomeFragment extends Fragment {
    Button btnTranslate,btn_ok;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        btnTranslate=view.findViewById(R.id.btn_translate);
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), TranslateActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}