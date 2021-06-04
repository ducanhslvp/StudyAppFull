package com.ducanh.appchat.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.ducanh.appchat.fragments.ProgressImage;
import com.ducanh.appchat.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    private Context context;
    private List<Chat> chats;
    private String imageURL;
    LayoutInflater inflater;

    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;


    public MessageAdapter(Context context, List<Chat> chats, String imageURL) {
        this.context = context;
        this.chats = chats;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view= LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat=chats.get(position);
        if (checkUri(chat.getMessage())==false){

            holder.imageMessage.setVisibility(View.GONE);
            holder.message.setVisibility(View.VISIBLE);
            holder.message.setText(chat.getMessage());
        }
        else{
            holder.message.setVisibility(View.GONE);
            holder.imageMessage.setVisibility(View.VISIBLE);
            Picasso.get().load(chat.getMessage()).into(holder.imageMessage);
            holder.imageMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayAlertDialog(chat.getMessage());

                }
            });


        }

        if (imageURL.equals("default")){
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(imageURL).into(holder.profileImage);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class  ViewHolder extends  RecyclerView.ViewHolder{
        public TextView message;
        public ImageView profileImage;
        public ImageView imageMessage;

        public ViewHolder(View itemView){
            super(itemView);
            message=itemView.findViewById(R.id.message);
            profileImage=itemView.findViewById(R.id.profile_image);
            imageMessage=itemView.findViewById(R.id.image_mesage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (chats.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
    private boolean checkUri(String mess){
        if (mess.indexOf("https://firebasestorage")==-1) return false;
        else return true;
    }
    public void displayAlertDialog(String imageURL) {
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertLayout = inflater.inflate(R.layout.imageview_dialog, null);
        final ImageView imageView = (ImageView) alertLayout.findViewById(R.id.imageViewMess);
        Picasso.get().load(imageURL).into(imageView);


        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Ảnh");
        alert.setView(alertLayout);
        alert.setCancelable(true);

        AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
    }

}
