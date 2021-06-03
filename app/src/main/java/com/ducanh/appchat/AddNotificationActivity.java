package com.ducanh.appchat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.ducanh.appchat.model.Noti;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import java.util.Calendar;

public class AddNotificationActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private Button btnAdd;
    private TimePicker timePicker;
    final String CHANNEL_ID = "101";
    private SQLiteHelper sqLiteHelper;
    private EditText editTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);

        datePicker = (DatePicker)findViewById(R.id.datePicker);
        timePicker = (TimePicker) this.findViewById(R.id.timePicker);
        editTitle=findViewById(R.id.edit_title);

        sqLiteHelper=new SQLiteHelper(this);


        btnAdd=findViewById(R.id.btn_addNoti);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Noti noti=new Noti(1,getDateFromDatePicker(),editTitle.getText().toString());
                sqLiteHelper.addNoti(noti);
//                sqLiteHelper.addDate(getDateFromDatePicker());
                Intent intent=new Intent(AddNotificationActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
    private String getDateFromDatePicker(){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();
        int gio =timePicker.getCurrentHour();
        int phut=timePicker.getCurrentMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day,gio,phut);
        System.out.println(day+"/"+month+"/"+year+"/"+gio+"/"+phut);

        return gio+"/"+phut+"/"+day+"/"+month+"/"+year;

//        return calendar.getTime().toString();
    }
//    private boolean checkDate(String key){
//        Calendar c = Calendar.getInstance();
//
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//
//        int day=c.get(Calendar.DAY_OF_MONTH);
//        int year =  c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//
//
//        String date[]=key.split("/");
//
//        final int ngay = Integer.parseInt(date[2]);
//        final int thang = Integer.parseInt(date[1]);
//        int gio = Integer.parseInt(date[0]);
//        int phut = Integer.parseInt(date[1]);
//
//        if () return true;
//        else return false;
//
//
//    }

}