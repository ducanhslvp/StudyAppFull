package com.ducanh.appchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ducanh.appchat.MessageActivity;
import com.ducanh.appchat.R;
import com.ducanh.appchat.model.Chat;
import com.ducanh.appchat.model.ChatList;
import com.ducanh.appchat.model.Subject;
import com.ducanh.appchat.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.ViewHolder> {
    private Context context;
    private List<User> users;
    private boolean isAdd;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    public AddFriendAdapter(Context context, List<User> users, boolean isAdd) {
        this.context = context;
        this.users = users;
        this.isAdd = isAdd;
    }

    @NonNull
    @Override
    public AddFriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_addfriend, parent, false);
        return new AddFriendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFriendAdapter.ViewHolder holder, int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        User user = users.get(position);
        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(user.getImageURL()).into(holder.profileImage);
        }
        if (isAdd==true){
            holder.btnAddFriend.setVisibility(View.VISIBLE);
            holder.btnConfirm.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);

            holder.btnAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //nguoi nhan addfriend nam ngoai
                    FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("AddFriends")
                            .child(user.getId()).child(firebaseUser.getUid());
                    chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()){
                                chatRef.child("id").setValue(firebaseUser.getUid());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });
        }else {
            holder.btnAddFriend.setVisibility(View.GONE);
            holder.btnConfirm.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);

            holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //them vao list chat
                    DatabaseReference reference =FirebaseDatabase.getInstance().getReference();
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("sender",firebaseUser.getUid());
                    hashMap.put("receiver",user.getId());
                    hashMap.put("message","Xin chào!");

                    reference.child("Chats").push().setValue(hashMap);

                    DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("ListChat")
                            .child(firebaseUser.getUid()).child(user.getId());
                    chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()){
                                chatRef.child("id").setValue(user.getId());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    DatabaseReference chatRef2=FirebaseDatabase.getInstance().getReference("ListChat")
                            .child(user.getId()).child(firebaseUser.getUid());
                    chatRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()){
                                chatRef2.child("id").setValue(firebaseUser.getUid());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    //xoa khoi AddFriendList

                    reference=FirebaseDatabase.getInstance().getReference("AddFriends").child(firebaseUser.getUid());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot snapshot1:snapshot.getChildren()) {
                                ChatList chatList = snapshot1.getValue(ChatList.class);
                                if (chatList.getId().equals(user.getId()))
                                    snapshot1.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            new AddFriendAdapter(context,users,false);
                                        }
                                    });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reference=FirebaseDatabase.getInstance().getReference("AddFriends").child(firebaseUser.getUid());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot snapshot1:snapshot.getChildren()) {
                                ChatList chatList = snapshot1.getValue(ChatList.class);
                                if (chatList.getId().equals(user.getId()))
                                    snapshot1.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            new AddFriendAdapter(context,users,false);
                                        }
                                    });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profileImage;
        private ImageView imgOff, imgOn;
        private Button btnAddFriend, btnConfirm, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profileImage = itemView.findViewById(R.id.profile_image);
            imgOn = itemView.findViewById(R.id.img_on);
            imgOff = itemView.findViewById(R.id.img_off);
            btnAddFriend = itemView.findViewById(R.id.btn_addFriend);
            btnConfirm = itemView.findViewById(R.id.btn_confirm);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

}
