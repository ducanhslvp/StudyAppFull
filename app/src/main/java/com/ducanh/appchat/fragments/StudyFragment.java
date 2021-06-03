package com.ducanh.appchat.fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ducanh.appchat.AddNotificationActivity;
import com.ducanh.appchat.AddSubjectActivity;
import com.ducanh.appchat.R;
import com.ducanh.appchat.SQLiteHelper;
import com.ducanh.appchat.adapter.SubjectAdapter;
import com.ducanh.appchat.model.Noti;
import com.ducanh.appchat.model.Subject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class StudyFragment extends Fragment {
    private RecyclerView recyclerView;
    SubjectAdapter subjectAdapter;
    List<Subject> subjects=new ArrayList<>();
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ImageButton btnAddNoti;
    TextView txtTime,txtNotiTile;
    Spinner spinner;
    private FloatingActionButton btnAdd;
    SQLiteHelper sqLiteHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_study, container, false);

        recyclerView=view.findViewById(R.id.recycler_view);
        btnAddNoti=view.findViewById(R.id.btn_addNoti);
        txtTime=view.findViewById(R.id.txt_time);
        txtNotiTile=view.findViewById(R.id.txt_notiTitle);
        sqLiteHelper=new SQLiteHelper(getContext());

        try {
            List<Noti> notiList=sqLiteHelper.getNoti();

            if (notiList!=null){
                Noti noti =notiList.get(notiList.size()-1);
                String time1=noti.getTime();
                String time[]=time1.split("/");
                txtTime.setText(time[0]+":"+time[1]+":"+"00");
                txtNotiTile.setText(noti.getTitle());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        btnAdd=view.findViewById(R.id.fab_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddSubjectActivity.class);
                startActivity(intent);
            }
        });
        btnAddNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddNotificationActivity.class);
                startActivity(intent);
            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getSubject();

        return view;
    }
    private void getSubject(){
//        List<String> subjectName=new ArrayList<>();
//        Query reference=FirebaseDatabase.getInstance().getReference("Subject").orderByChild("question").equalTo("ggggg");
        reference=FirebaseDatabase.getInstance().getReference("Subjects");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjects.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Subject subject=snapshot1.getValue(Subject.class);
                    subjects.add(subject);
//                    subjectName.add(subject.getName());

                }
                subjectAdapter=new SubjectAdapter(getContext(),subjects);
                recyclerView.setAdapter(subjectAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}