package com.ducanh.appchat.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ducanh.appchat.R;
import com.ducanh.appchat.adapter.UserAdapter;
import com.ducanh.appchat.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class UsersFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> users;
    EditText searchUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_users,container,false);

        recyclerView=view.findViewById(R.id.recycler_view);
        searchUser=view.findViewById(R.id.search_user);
        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUserVoid(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        users=new ArrayList<>();
        readUsers();
        return view;
    }
    private void searchUserVoid(String s){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Query query=FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(s).endAt(s+"\uf0ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!searchUser.getText().toString().equals("")){
                    users.clear();
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        User user=snapshot1.getValue(User.class);
                        assert  user!=null;
                        if (!user.getId().equals(firebaseUser.getUid())){
                            users.add(user);
                        }
                    }
                    userAdapter=new UserAdapter(getContext(),users,false);
                    recyclerView.setAdapter(userAdapter);
                }else  readUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readUsers(){
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    User user=snapshot1.getValue(User.class);

                    assert user!=null;
                    if (!user.getId().equals(firebaseUser.getUid())){
                        users.add(user);
                    }
                }
                userAdapter=new UserAdapter(getContext(),users,false);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}