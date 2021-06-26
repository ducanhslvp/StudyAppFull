package com.ducanh.appchat.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.ducanh.appchat.R;
import com.ducanh.appchat.activity.AddClassActivity;
import com.ducanh.appchat.adapter.AddFriendAdapter;
import com.ducanh.appchat.adapter.ClassAdapter;
import com.ducanh.appchat.model.ChatList;
import com.ducanh.appchat.model.Class;
import com.ducanh.appchat.model.ClassFeed;
import com.ducanh.appchat.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ClassFragment extends Fragment {
    private RecyclerView recyclerView;
    EditText txtSearch;
    ClassAdapter classAdapter;
//    List<String> listClass=new ArrayList<>();
    List<ChatList> listClass=new ArrayList<>();
    List<ChatList> listClassCheck=new ArrayList<>();
    String className;
    Spinner spinner;
    private FloatingActionButton btnAdd;
    private String role;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_class, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        txtSearch=view.findViewById(R.id.edit_search);
        btnAdd=view.findViewById(R.id.fab_add);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        Intent intent=new Intent();
//        intent=getActivity().getIntent();
//        role=intent.getStringExtra("role");

//        setSpinner();
        getClassNameFromUser();



        if (getRole()){

            txtSearch.setVisibility(View.GONE);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent=new Intent(getContext(), AddClassActivity.class);
//                    startActivity(intent);

                    addClassDialog();

                }
            });
        }else{
            btnAdd.setVisibility(View.GONE);

            txtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchClassFromAllClass(s.toString().toLowerCase());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }


        return  view;
    }
    public void addClassDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_edit_feed, null);
        final EditText content = (EditText) alertLayout.findViewById(R.id.edit_feed);

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Thêm lớp mới");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Quay lại", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Quay lại", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference classRef=FirebaseDatabase.getInstance().getReference("Class")
                        .child(content.getText().toString());

                Calendar c = Calendar.getInstance();
                int hour, minute, second,day,month;
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
                second = c.get(Calendar.SECOND);
                day=c.get(Calendar.DAY_OF_MONTH);
                month=c.get(Calendar.MONTH);
                String date=minute+"/"+hour+"/"+day+"/"+month;

                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("userID",firebaseUser.getUid());
                hashMap.put("content","Đã tạo lớp!");
                hashMap.put("type","text");
                hashMap.put("date",date);

                classRef.push().setValue(hashMap);
                addToClassUser(content.getText().toString());
                addToClassName(content.getText().toString());

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }
    private void addToClassUser(String className){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference classRef=FirebaseDatabase.getInstance().getReference("ClassUser")
                .child(firebaseUser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("id",className);
        classRef.push().setValue(hashMap);

    }
    private void addToClassName(String className){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference classRef=FirebaseDatabase.getInstance().getReference("ClassName");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("name",className);

        classRef.push().setValue(hashMap);
    }
    private void searchClassFromAllClass(String s){
        s=s.toUpperCase();
        Query query=FirebaseDatabase.getInstance().getReference("ClassName").orderByChild("name")
                .startAt(s).endAt(s+"\uf0ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!txtSearch.getText().toString().equals("")) {
                    listClass.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Class classs = snapshot1.getValue(Class.class);
                        ChatList className=new ChatList(classs.getName());

                        boolean kt=true;
                        for (ChatList check:listClassCheck)
                            if (check.getId().equals(className.getId()))
                                kt=false;
                        if (kt)  listClass.add(className);

                    }
                    classAdapter=new ClassAdapter(getContext(),listClass,false);
                    recyclerView.setAdapter(classAdapter);
                } else  getClassNameFromUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    private void getClassNameFromUser(){
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
                listClassCheck.addAll(listClass);
                classAdapter=new ClassAdapter(getContext(),listClass,true);
                recyclerView.setAdapter(classAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public boolean getRole(){
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("roleApp", Context.MODE_PRIVATE);
        if(sharedPreferences!= null) {
            return sharedPreferences.getBoolean("role", false);
        }else return false;
    }
}