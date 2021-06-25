package com.ducanh.appchat.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ducanh.appchat.R;
import com.ducanh.appchat.TranslateActivity;
import com.ducanh.appchat.adapter.SubjectAdapter;
import com.ducanh.appchat.model.Point;
import com.ducanh.appchat.model.Subject;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {
    Button btnTranslate,btn_ok;
    List<Subject> subjects=new ArrayList<>();
    DatabaseReference reference;
    BarChart barChart;
    TextView txtCount,txtTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);

         barChart=(BarChart) view.findViewById(R.id.barchart);
         txtCount=view.findViewById(R.id.txt_count);
         txtTime=view.findViewById(R.id.txt_timeBest);

        getSubject();
        setCount();


        return view;
    }
    private void getSubject(){
//        List<String> subjectName=new ArrayList<>();
//        Query reference=FirebaseDatabase.getInstance().getReference("Subject").orderByChild("question").equalTo("ggggg");
        reference= FirebaseDatabase.getInstance().getReference("Subjects");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjects.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Subject subject=snapshot1.getValue(Subject.class);
                    subjects.add(subject);

                }
                ArrayList<BarEntry> barEntries = new ArrayList<>();

                for (int i=0;i<subjects.size();i++){
                    Random generator = new Random();
                    float value = generator.nextFloat() *3+6;

                    barEntries.add(new BarEntry((float) i,value));

                }

                BarDataSet barDataSet = new BarDataSet(barEntries, "Môn học");

                ArrayList<String> theDates = new ArrayList<>();
                for (int i=0;i<subjects.size();i++){
                    String name1=subjects.get(i).getName();
                    String name[]=name1.split(" ");
                    String out1="";
                    for (int j=0;j<name.length;j++){
                        String outName=name[j].substring(0, 1);
                        outName=outName.toUpperCase();
                        out1+=outName;
                    }
                    theDates.add(out1);

                }

                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(theDates));
                BarData theData = new BarData(barDataSet);//----Line of error
                barChart.setData(theData);
                barChart.setTouchEnabled(true);
                barChart.setDragEnabled(true);
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                barChart.setScaleEnabled(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void setCount(){
        List<Point> points=new ArrayList<>();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference("Points").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                points.clear();
                float max=0;
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Point point =snapshot1.getValue(Point.class);
                    points.add(point);
                    float pointInt=Float.parseFloat(point.getPoint());
                    if (pointInt>max) max=pointInt;
                }
                txtCount.setText(points.size()+"");
                txtTime.setText(max+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}