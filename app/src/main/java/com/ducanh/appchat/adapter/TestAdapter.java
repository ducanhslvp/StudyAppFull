package com.ducanh.appchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ducanh.appchat.R;
import com.ducanh.appchat.TestActivity;
import com.ducanh.appchat.model.Subject;
import com.ducanh.appchat.model.Test;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    private Context context;
    private List<Test> listTest;

    public TestAdapter(Context context, List<Test> listTest) {
        this.context = context;
        this.listTest = listTest;
    }

    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_test,parent,false);
        return new TestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHolder holder, int position) {
        Test test=listTest.get(position);
        holder.txtQuestion.setText(test.getQuestion().getQuestion().toString());
        holder.radioA.isChecked();
        System.out.println("chon-------------------A");

    }

    @Override
    public int getItemCount() {
        return listTest.size();
    }

    public class  ViewHolder extends  RecyclerView.ViewHolder{
        public TextView txtQuestion;
        public RadioButton radioA;

        public ViewHolder(View itemView){
            super(itemView);
            txtQuestion=itemView.findViewById(R.id.txt_question);
            radioA=itemView.findViewById(R.id.radio_A);

        }
    }
}
