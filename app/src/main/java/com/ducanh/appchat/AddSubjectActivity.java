package com.ducanh.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ducanh.appchat.model.Question;
import com.ducanh.appchat.model.Subject;
import com.ducanh.appchat.model.Test;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddSubjectActivity extends AppCompatActivity {
    private EditText txtSubjectname,txtQuestion,txtAnswer,txtTestName;
    TextView txtSubjectName2,txtTestName2;
    private Button btnAddTest,btnAddSubject;
    Spinner subjectSpinner,testSpinner;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        txtSubjectname=findViewById(R.id.edit_subjectName);
        txtQuestion=findViewById(R.id.edit_question);
        txtAnswer=findViewById(R.id.edit_answer);
        subjectSpinner=findViewById(R.id.subject_spinner);
        testSpinner=findViewById(R.id.test_spinner);

        txtTestName2=findViewById(R.id.txt_testName);
        txtSubjectName2=findViewById(R.id.txt_subjectName);

        btnAddTest=findViewById(R.id.btn_addTest);
        btnAddSubject=findViewById(R.id.btn_addSubject);

        setSpinner();

//        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());


        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectName=txtSubjectname.getText().toString();
                Subject subject=new Subject(subjectName,"0");
                addSubject(subject);
            }
        });

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Adapter adapter = parent.getAdapter();
                txtSubjectName2.setText(adapter.getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        testSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Adapter adapter = parent.getAdapter();
                txtTestName2.setText(adapter.getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnAddTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question=txtQuestion.getText().toString();
                String answer=txtAnswer.getText().toString();
                String subjectName=txtSubjectName2.getText().toString();
                String testName=txtTestName2.getText().toString();


                if (TextUtils.isEmpty((subjectName)) || TextUtils.isEmpty(question)
                        || TextUtils.isEmpty(answer)){
                    Toast.makeText(AddSubjectActivity.this, "Hãy nhập tất cả các dòng",Toast.LENGTH_SHORT).show();

                }else{
                    Question question1=new Question(question,answer);
                    Test test=new Test(testName,subjectName,question1);
                    addTest(test);
                }

            }
        });

    }
    public void setSpinner(){
        getSubject();
        String tests[]={"15 phut","Giua ky","Hoc ky"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                tests);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.testSpinner.setAdapter(adapter2);

    }

    private void getSubject(){
//        List<Subject> subjects=new ArrayList<>();
        List<String> subjectName=new ArrayList<>();
//        Query reference=FirebaseDatabase.getInstance().getReference("Subject").orderByChild("question").equalTo("ggggg");
        reference=FirebaseDatabase.getInstance().getReference("Subjects");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectName.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Subject subject=snapshot1.getValue(Subject.class);
//                    subjects.add(subject);
                    subjectName.add(subject.getName());

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddSubjectActivity.this,
                        android.R.layout.simple_spinner_item,
                        subjectName);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subjectSpinner.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private List<Test> getTest(){
        List<Test> listTest=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Tests");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTest.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Test test=snapshot1.getValue(Test.class);
                    listTest.add(test);
                    System.out.println(test +"   "+ test.getQuestion().getQuestion());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listTest;
    }
    private void addSubject(Subject subject) {
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("name",subject.getName());
        hashMap.put("point",subject.getPoint());
        FirebaseDatabase.getInstance().getReference().child("Subjects").push().setValue(hashMap);
    }
    private void addTest(Test test) {

        HashMap<String,Object> hashMap2=new HashMap<>();
        hashMap2.put("name",test.getName());
        hashMap2.put("subjectName",test.getSubjectName());
        hashMap2.put("question",test.getQuestion());
        FirebaseDatabase.getInstance().getReference().child("Tests").push().setValue(hashMap2);
    }



}