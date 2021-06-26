package com.ducanh.appchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ducanh.appchat.MessageActivity;
import com.ducanh.appchat.R;
import com.ducanh.appchat.TestActivity;
import com.ducanh.appchat.model.Subject;
import com.ducanh.appchat.model.User;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    private Context context;
    private List<Subject> subjects;

    public SubjectAdapter(Context context, List<Subject> subjects) {
        this.context = context;
        this.subjects = subjects;
    }

    @NonNull
    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_subject,parent,false);
        return new SubjectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.ViewHolder holder, int position) {
        Subject subject=subjects.get(position);
        holder.subjectName.setText(subject.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, TestActivity.class);
                intent.putExtra("subjectName", subject.getName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public class  ViewHolder extends  RecyclerView.ViewHolder{
        public TextView subjectName;

        public ViewHolder(View itemView){
            super(itemView);
            subjectName=itemView.findViewById(R.id.txt_subjectName);

        }
    }

}
