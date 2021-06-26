package com.ducanh.appchat.activity;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ducanh.appchat.R;
import com.ducanh.appchat.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetRole {
    private boolean kt;

    public GetRole() {
        getRole();
        System.out.println(kt+"chay qua day=========2");
    }

    public void getRole(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if (user.getRole().equals("admin")) setKt(true);
                System.out.println(kt+"chay qua day=========1");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public boolean isKt() {
        return kt;
    }

    public void setKt(boolean kt) {
        this.kt = kt;
    }
}
