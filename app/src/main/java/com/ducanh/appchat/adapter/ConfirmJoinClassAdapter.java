package com.ducanh.appchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ducanh.appchat.R;
import com.ducanh.appchat.model.ChatList;
import com.ducanh.appchat.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ConfirmJoinClassAdapter extends RecyclerView.Adapter<ConfirmJoinClassAdapter.ViewHolder> {
    private Context context;
    private List<User> users;
    private String className;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    public ConfirmJoinClassAdapter(Context context, List<User> users, String className) {
        this.context = context;
        this.users = users;
        this.className = className;
    }

    @NonNull
    @Override
    public ConfirmJoinClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_join_class, parent, false);
        return new ConfirmJoinClassAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmJoinClassAdapter.ViewHolder holder, int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        User user = users.get(position);
        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(user.getImageURL()).into(holder.profileImage);
        }
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference classRef=FirebaseDatabase.getInstance().getReference("Class")
                        .child(className);

                Calendar c = Calendar.getInstance();
                int hour, minute, second,day,month;
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
                second = c.get(Calendar.SECOND);
                day=c.get(Calendar.DAY_OF_MONTH);
                month=c.get(Calendar.MONTH);
                String date=minute+"/"+hour+"/"+day+"/"+month;

                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("userID",user.getId());
                hashMap.put("content","Đã vào lớp!");
                hashMap.put("type","text");
                hashMap.put("date",date);

                classRef.push().setValue(hashMap);
                addToClassUser(user.getId());

                delete(user.getId());


            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(user.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profileImage;
        private Button btnConfirm, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profileImage = itemView.findViewById(R.id.profile_image);
            btnConfirm = itemView.findViewById(R.id.btn_confirm);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
    private void addToClassUser(String userID){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference classRef=FirebaseDatabase.getInstance().getReference("ClassUser")
                .child(userID);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("id",className);
        classRef.push().setValue(hashMap);

    }
    private void delete(String userID){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("UserWaitClass").child(className);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1:snapshot.getChildren()) {
                    ChatList chatList = snapshot1.getValue(ChatList.class);
                    if (chatList.getId().equals(userID))
                        snapshot1.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                new ConfirmJoinClassAdapter(context,users,className);
                            }
                        });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
