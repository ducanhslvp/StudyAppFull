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

import com.bumptech.glide.Glide;
import com.ducanh.appchat.MessageActivity;
import com.ducanh.appchat.R;
import com.ducanh.appchat.model.User;

import java.io.Serializable;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<User> users;
    private  boolean isChat;

    public UserAdapter(Context context,List<User> users,boolean isChat){
        this.users=users;
        this.context=context;
        this.isChat=isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user=users.get(position);
        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")){
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(user.getImageURL()).into(holder.profileImage);
        }
        if (isChat){
            if (user.getStatus().equals("online")){
                holder.imgOn.setVisibility(View.VISIBLE);
                holder.imgOff.setVisibility(View.GONE);
            }
            else{
                holder.imgOn.setVisibility(View.GONE);
                holder.imgOff.setVisibility(View.VISIBLE);
            }
        }else {
            holder.imgOn.setVisibility(View.GONE);
            holder.imgOff.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MessageActivity.class);
                intent.putExtra("user", user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class  ViewHolder extends  RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profileImage;
        private ImageView imgOff,imgOn;

        public ViewHolder(View itemView){
            super(itemView);
            username=itemView.findViewById(R.id.username);
            profileImage=itemView.findViewById(R.id.profile_image);
            imgOn=itemView.findViewById(R.id.img_on);
            imgOff=itemView.findViewById(R.id.img_off);
        }
    }

}