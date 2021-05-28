package com.ducanh.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ducanh.appchat.adapter.SubjectAdapter;
import com.ducanh.appchat.adapter.TestAdapter;
import com.ducanh.appchat.model.Subject;
import com.ducanh.appchat.model.Test;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    TestAdapter testAdapter;
    List<Test> listTest=new ArrayList<>();
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getTest();

    }
    private void getTest(){
        reference= FirebaseDatabase.getInstance().getReference("Tests");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTest.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Test test=snapshot1.getValue(Test.class);
                    listTest.add(test);

                }
                testAdapter =new TestAdapter(TestActivity.this,listTest);
                recyclerView.setAdapter(testAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}