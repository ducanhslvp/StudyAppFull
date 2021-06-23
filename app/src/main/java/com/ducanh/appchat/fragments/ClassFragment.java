package com.ducanh.appchat.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ducanh.appchat.AddSubjectActivity;
import com.ducanh.appchat.R;
import com.ducanh.appchat.adapter.ClassAdapter;
import com.ducanh.appchat.adapter.UserAdapter;
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

public class ClassFragment extends Fragment {
    private RecyclerView recyclerView;
    EditText txtSearch;
    ClassAdapter classAdapter;
//    List<String> listClass=new ArrayList<>();
    List<ChatList> listClass=new ArrayList<>();
    String className;
    Spinner spinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_class, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        txtSearch=view.findViewById(R.id.edit_search);

//        Intent intent=new Intent();
//        intent=getActivity().getIntent();
//        className=intent.getStringExtra("class");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        setSpinner();


        getClassName();


        return  view;
    }

//    private void setSpinner() {
//
//        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("ClassUser").child(firebaseUser.getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                listClass.clear();
//                for (DataSnapshot snapshot1:snapshot.getChildren()){
//                    ChatList classs=snapshot1.getValue(ChatList.class);
//                    listClass.add(classs.getId());
//                }
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
//                        android.R.layout.simple_spinner_item,
//                        listClass);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner.setAdapter(adapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Adapter adapter = parent.getAdapter();
//                txtClassName.setText(adapter.getItem(position).toString());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//    }

    private void getClassName(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("ClassUser").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listClass.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    ChatList classs=snapshot1.getValue(ChatList.class);
                    listClass.add(classs);
                }
                classAdapter=new ClassAdapter(getContext(),listClass);
                recyclerView.setAdapter(classAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}