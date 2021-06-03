package com.ducanh.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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
    Button btnCaculatrPoint,btnViewAnswer;
    TextView txtPoint, txtCorrect,txtCoundown;
    TestAdapter testAdapter;
    List<Test> listTest=new ArrayList<>();
    List<Test> listTest2=new ArrayList<>();
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Spinner spinner;
    CountDownTimer Timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        recyclerView=findViewById(R.id.recycler_view);
        txtPoint=findViewById(R.id.txt_point);
        txtCorrect=findViewById(R.id.txt_correct);
        btnViewAnswer=findViewById(R.id.btn_viewAnswerCorrect);
        txtCoundown=findViewById(R.id.txt_coundown);

        Timer = new CountDownTimer(30*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtCoundown.setText("Thời gian: "+String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                txtCoundown.setText("Hết giờ");
                viewAnswer();
            }
        }.start();


        spinner=findViewById(R.id.spinner_ky);

        String tests[]={"Tất cả","15 phut","Giua ky","Hoc ky"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                tests);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //-dem nguoc
                Timer.cancel();
                Timer = new CountDownTimer(30*1000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        txtCoundown.setText("Thời gian: "+String.valueOf(millisUntilFinished/1000));
                    }

                    @Override
                    public void onFinish() {
                        txtCoundown.setText("Hết giờ");
                        viewAnswer();
                    }
                }.start();

                listTest2.clear();
                if (position==0) listTest2.addAll(listTest);
                else{
                    String name=tests[position];
                    for (Test test:listTest){
                        if (test.getName().equals(name))
                            listTest2.add(test);
                    }
                }
                testAdapter =new TestAdapter(TestActivity.this,listTest2,false);
                recyclerView.setAdapter(testAdapter);
                btnViewAnswer.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getTest();

//        btnCaculatrPoint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String[] listAnswer=testAdapter.getListAnswer();
//
//
//            }
//        });
        btnViewAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAnswer();
                Timer.cancel();
            }
        });

    }
    private void viewAnswer(){
        String[] listAnswer=testAdapter.getListAnswer();

        int correct=0;
        for (int i=0;i<listTest2.size();i++)
            if (listAnswer[i].equals(listTest2.get(i).getQuestion().getAnswer())){
                correct++;
            }
        txtCorrect.setText("Correct: "+correct+"/"+listTest2.size());
        txtPoint.setText("Point: "+((float) correct/listTest2.size()*10));

        testAdapter =new TestAdapter(TestActivity.this,listTest2,listAnswer,true);
        recyclerView.setAdapter(testAdapter);
        btnViewAnswer.setEnabled(false);
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
                listTest2.addAll(listTest);

                testAdapter =new TestAdapter(TestActivity.this,listTest2,false);
                recyclerView.setAdapter(testAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}