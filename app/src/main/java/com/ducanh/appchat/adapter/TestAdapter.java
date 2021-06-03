package com.ducanh.appchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ducanh.appchat.R;
import com.ducanh.appchat.TestActivity;
import com.ducanh.appchat.model.Subject;
import com.ducanh.appchat.model.Test;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    private Context context;
    private List<Test> listTest;
    public String[] listAnswer=new String[50];
    public boolean check;

    public TestAdapter(Context context, List<Test> listTest, boolean check) {
        this.context = context;
        this.listTest = listTest;
        this.check = check;
    }

    public TestAdapter(Context context, List<Test> listTest, String[] listAnswer, boolean check) {
        this.context = context;
        this.listTest = listTest;
        this.listAnswer = listAnswer;
        this.check = check;
    }

    public String[] getListAnswer(){
        return listAnswer;
    }


    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_test,parent,false);
        return new TestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHolder holder, int position) {
        if (check==true){
            if (!listTest.get(position).getQuestion().getAnswer().equals(listAnswer[position])){
                holder.txtQuestion.setTextColor(Color.RED);
                //set dap an dung
                String correct=listTest.get(position).getQuestion().getAnswer();
                if (correct.equals("A")) {

                    holder.radioA.setTextColor(Color.GREEN);
                }else
                if (correct.equals("B")) {

                    holder.radioB.setTextColor(Color.GREEN);
                }else
                if (correct.equals("C")) {

                    holder.radioC.setTextColor(Color.GREEN);
                }
                //set dap an sai
                String yourAnswer=listAnswer[position];
                if (yourAnswer.equals("A")) {
                    holder.radioA.setChecked(true);
                    holder.radioA.setTextColor(Color.RED);
                }else
                if (yourAnswer.equals("B")) {
                    holder.radioB.setChecked(true);
                    holder.radioB.setTextColor(Color.RED);
                }else
                if (yourAnswer.equals("C")) {
                    holder.radioC.setChecked(true);
                    holder.radioC.setTextColor(Color.RED);
                }
            }
            //set truong hop chon dung dap an
            else {
                holder.txtQuestion.setTextColor(Color.GREEN);
                String correct=listTest.get(position).getQuestion().getAnswer();
                if (correct.equals("A")) {
                    holder.radioA.setChecked(true);
                    holder.radioA.setTextColor(Color.GREEN);
                }else
                if (correct.equals("B")) {
                    holder.radioB.setChecked(true);
                    holder.radioB.setTextColor(Color.GREEN);
                }else
                if (correct.equals("C")) {
                    holder.radioC.setChecked(true);
                    holder.radioC.setTextColor(Color.GREEN);
                }
            }

        }
        listAnswer[position]="0";
        Test test=listTest.get(position);
        holder.txtQuestion.setText(test.getQuestion().getQuestion().toString());
        holder.radioA.setText(listTest.get(position).getQuestion().getAnswerA());
        holder.radioB.setText(listTest.get(position).getQuestion().getAnswerB());
        holder.radioC.setText(listTest.get(position).getQuestion().getAnswerC());



        holder.radioA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAnswer[position]="A";
            }
        });
        holder.radioB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAnswer[position]="B";
            }
        });
        holder.radioC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAnswer[position]="C";
            }
        });

    }

    @Override
    public int getItemCount() {
        return listTest.size();
    }

    public class  ViewHolder extends  RecyclerView.ViewHolder{
        public TextView txtQuestion;
        public RadioButton radioA,radioB,radioC,radioD;

        public ViewHolder(View itemView){
            super(itemView);
            txtQuestion=itemView.findViewById(R.id.txt_question);
            radioA=itemView.findViewById(R.id.radio_A);
            radioB=itemView.findViewById(R.id.radio_B);
            radioC=itemView.findViewById(R.id.radio_C);
//            radioD=itemView.findViewById(R.id.radio_D);
        }
    }
}
