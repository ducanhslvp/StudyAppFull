package com.ducanh.appchat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ducanh.appchat.R;
import com.ducanh.appchat.adapter.AddFriendAdapter;
import com.ducanh.appchat.adapter.ClassAdapter;
import com.ducanh.appchat.adapter.ConfirmJoinClassAdapter;
import com.ducanh.appchat.model.ChatList;
import com.ducanh.appchat.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConfirmUserJoinClassActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ConfirmJoinClassAdapter adapter;
    List<User> users=new ArrayList<>();
    List<ChatList> userList=new ArrayList<>();
    DatabaseReference reference;
    String className;
    Button btnConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_user_join_class);
        recyclerView=findViewById(R.id.recycler_view);
        btnConfirm=findViewById(R.id.btn_confirm);

        Intent intent=new Intent();
        intent=getIntent();
        className=intent.getStringExtra("className");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ConfirmUserJoinClassActivity.this));
        readUsers();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUsers();
            }
        });

    }

    private void readAllUsers(){

    }
    private void readUsers(){
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("UserWaitClass").child(className);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()) {
                    ChatList chatList = snapshot1.getValue(ChatList.class);
                    userList.add(chatList);
//                    System.out.println(chatList.getId()+"chat lit============");
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void chatList(){
        users=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    User user=snapshot1.getValue(User.class);
                    for (ChatList chatList:userList){
                        if (user.getId().equals(chatList.getId())){
                            users.add(user);
                        }
                    }
                }
                adapter=new ConfirmJoinClassAdapter(ConfirmUserJoinClassActivity.this,users,className);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}