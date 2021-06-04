package com.ducanh.appchat.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

import com.ducanh.appchat.R;

public class ProgressImage extends DialogFragment {
    ImageView imageView;
    int position=0;
    Activity activity;
    AlertDialog dialog;

    public ProgressImage() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.imageview_dialog, container);
        imageView = (ImageView) view.findViewById(R.id.imageViewMess);

        getDialog().setCancelable(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageView.setImageResource(R.mipmap.ic_launcher);
    }

}
