package com.ducanh.appchat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ducanh.appchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddClassActivity extends AppCompatActivity {
    private Button btnAddClass,btnJoinClass;
    private EditText txtClassName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        btnAddClass=findViewById(R.id.btn_addClass);
        txtClassName=findViewById(R.id.edit_className);
        btnJoinClass=findViewById(R.id.btn_joinClass);

        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference classRef=FirebaseDatabase.getInstance().getReference("Class")
                        .child(txtClassName.getText().toString()).child(firebaseUser.getUid());

                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("userID",firebaseUser.getUid());
                hashMap.put("content","Đã tạo lớp!");
                hashMap.put("type","text");

                classRef.push().setValue(hashMap);
                addToClassUser();
                addToClassName();


            }
        });
        btnJoinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference classRef=FirebaseDatabase.getInstance().getReference("Class")
                        .child(txtClassName.getText().toString()).child(firebaseUser.getUid());

                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("userID",firebaseUser.getUid());
                hashMap.put("content","Đã vào lớp!");
                hashMap.put("type","text");

                classRef.push().setValue(hashMap);
                addToClassUser();
            }
        });

    }
    private void addToClassUser(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference classRef=FirebaseDatabase.getInstance().getReference("ClassUser")
                .child(firebaseUser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("id",txtClassName.getText().toString());
        classRef.push().setValue(hashMap);

    }
    private void addToClassName(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference classRef=FirebaseDatabase.getInstance().getReference("ClassName");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("name",txtClassName.getText().toString());

        classRef.push().setValue(hashMap);
    }
}