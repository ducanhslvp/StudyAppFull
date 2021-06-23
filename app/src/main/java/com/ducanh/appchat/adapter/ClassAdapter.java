package com.ducanh.appchat.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ducanh.appchat.MessageActivity;
import com.ducanh.appchat.R;
import com.ducanh.appchat.activity.ClassActivity;
import com.ducanh.appchat.fragments.ClassFragment;
import com.ducanh.appchat.model.Chat;
import com.ducanh.appchat.model.ChatList;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {

    private Context context;
    private List<ChatList> listClass;

    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    public ClassAdapter(Context context, List<ChatList> listClass) {
        this.context = context;
        this.listClass = listClass;
    }

    @NonNull
    @Override
    public ClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.item_class,parent,false);
            return new ClassAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter.ViewHolder holder, int position) {
        ChatList classs=listClass.get(position);
        holder.txtClassName.setText(classs.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ClassActivity.class);
                intent.putExtra("class",classs.getId() );
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listClass.size();
    }

    public class  ViewHolder extends  RecyclerView.ViewHolder{
        public TextView txtClassName;

        public ViewHolder(View itemView){
            super(itemView);
            txtClassName=itemView.findViewById(R.id.txt_className);

        }
    }
}
