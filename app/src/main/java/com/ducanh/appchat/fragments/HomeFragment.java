package com.ducanh.appchat.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ducanh.appchat.R;
import com.ducanh.appchat.TranslateActivity;
import com.ducanh.appchat.adapter.SubjectAdapter;
import com.ducanh.appchat.model.Point;
import com.ducanh.appchat.model.Subject;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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
    private CombinedChart mChart;
    private int textColor=Color.DKGRAY;
    List<Point> points=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);

         barChart=(BarChart) view.findViewById(R.id.barchart);
         mChart = (CombinedChart) view.findViewById(R.id.combinedChart);

         txtCount=view.findViewById(R.id.txt_count);
         txtTime=view.findViewById(R.id.txt_timeBest);

//        getSubject();
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

                if (points.size()>=3){
                    for (int i=0;i<points.size()-3;i++){
                        points.remove(i);
                    }
                }

                outChart(points);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void outChart(List<Point> points){

        System.out.println(points.size()+"=====================");
        float[] point = new float[50];
        int d=points.size();

        final List<String> subjectNames = new ArrayList<>();

        for (int i=0;i<points.size();i++){
            point[i]=Float.parseFloat(points.get(i).getPoint());
            String name1=points.get(i).getSubject();
            String name[]=name1.split(" ");
            String out1="";
            for (int j=0;j<name.length;j++){
                String outName=name[j].substring(0, 1);
                outName=outName.toUpperCase();
                out1+=outName;
            }

            subjectNames.add(out1);
        }



        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
//        mChart.setOnChartValueSelectedListener(this);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(6f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return subjectNames.get((int) value % subjectNames.size());
            }
        });

        CombinedData data = new CombinedData();
        LineData lineDatas = new LineData();
        lineDatas.addDataSet((ILineDataSet) dataChart(textColor,point,d));

        data.setData(lineDatas);

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        mChart.setData(data);
        mChart.invalidate();
    }

    public void onValueSelected(Entry e, Highlight h) {
        Toast.makeText(getContext(), "Value: " + e.getY() + ", index: "
                + h.getX()
                + ", DataSet index: "
                + h.getDataSetIndex(), Toast.LENGTH_SHORT).show();
    }

    public void onNothingSelected() {

    }
    private static DataSet dataChart(int textColor, float [] data, int size) {

        LineData d = new LineData();


        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index <size; index++) {
            entries.add(new Entry(index, data[index]));
        }

        LineDataSet set = new LineDataSet(entries, "Điểm");
        set.setColor(textColor);
        set.setLineWidth(3f);
        set.setCircleColor(textColor);
        set.setCircleRadius(5f);
        set.setFillColor(textColor);
//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(textColor);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return set;
    }
}