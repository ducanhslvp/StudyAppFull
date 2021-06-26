package com.ducanh.appchat.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.ducanh.appchat.activity.ClassActivity;
import com.ducanh.appchat.fragments.ClassFragment;
import com.ducanh.appchat.model.Chat;
import com.ducanh.appchat.model.ChatList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {

    private Context context;
    private List<ChatList> listClass;
    private boolean isInClass;

    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    public ClassAdapter(Context context, List<ChatList> listClass, boolean isInClass) {
        this.context = context;
        this.listClass = listClass;
        this.isInClass = isInClass;
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
        if (isInClass) {
            holder.btnJoinClass.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }
        else{
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnJoinClass.setVisibility(View.VISIBLE);

            holder.btnJoinClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinClass(classs.getId().toString());
                    holder.btnDelete.setVisibility(View.VISIBLE);
                    holder.btnJoinClass.setVisibility(View.GONE);

                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(classs.getId());
                    holder.btnDelete.setVisibility(View.GONE);
                    holder.btnJoinClass.setVisibility(View.VISIBLE);
                }
            });

        }


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
        public Button btnJoinClass,btnDelete;

        public ViewHolder(View itemView){
            super(itemView);
            txtClassName=itemView.findViewById(R.id.txt_className);
            btnJoinClass=itemView.findViewById(R.id.btn_joinClass);
            btnDelete=itemView.findViewById(R.id.btn_deleteClass);
        }
    }
    private void joinClass(String className){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference chatRef= FirebaseDatabase.getInstance().getReference("UserWaitClass")
                .child(className).child(firebaseUser.getUid());
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
    private void delete(String className){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("UserWaitClass").child(className);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1:snapshot.getChildren()) {
                    ChatList chatList = snapshot1.getValue(ChatList.class);
                    if (chatList.getId().equals(firebaseUser.getUid()))
                        snapshot1.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
//                                new ConfirmJoinClassAdapter(context,users,className);
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
