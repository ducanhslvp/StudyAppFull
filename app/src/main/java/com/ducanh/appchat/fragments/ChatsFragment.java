package com.ducanh.appchat.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ducanh.appchat.R;
import com.ducanh.appchat.adapter.UserAdapter;
import com.ducanh.appchat.model.ChatList;
import com.ducanh.appchat.model.User;
import com.ducanh.appchat.notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> users;
     FirebaseUser firebaseUser;
     DatabaseReference reference;
     private List<ChatList> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        userList=new ArrayList<>();

//        reference=FirebaseDatabase.getInstance().getReference("Chats");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userList.clear();
//                for (DataSnapshot snapshot1:snapshot.getChildren()){
//                    Chat chat=snapshot1.getValue(Chat.class);
//
//                    if (chat.getSender().equals(firebaseUser.getUid())){
//                        userList.add(chat.getReceiver());
//                    }
//                    if (chat.getReceiver().equals(firebaseUser.getUid())){
//                        userList.add(chat.getSender());
//                    }
//                }
//                readChats();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        reference=FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()) {
                    ChatList chatList = snapshot1.getValue(ChatList.class);
                    userList.add(chatList);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }
    private  void updateToken(String token){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);

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
                userAdapter=new UserAdapter(getContext(),users,true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void readChats(){
//        users=new ArrayList<>();
//        reference=FirebaseDatabase.getInstance().getReference("Users");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                users.clear();
//                for (DataSnapshot snapshot1:snapshot.getChildren()){
//
//                    User user=snapshot1.getValue(User.class);
//                    //hien thi user
//                    for (String id:userList){
//                        System.out.println("+++++++++++++++++++++");
//                        if (user.getId().equals(id)){
//                            if (users.size()!=0){
//                                for (int i=0;i<users.size();i++){
//                                    if (!user.getId().equals(users.get(i).getId())){
//                                        users.add(user);
//                                    }
//                                }
//                            }else{
//                                users.add(user);
//                            }
//                        }
//                    }
//                }
//                userAdapter=new UserAdapter(getContext(),users,true);
//                recyclerView.setAdapter(userAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}