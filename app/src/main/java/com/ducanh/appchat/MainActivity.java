package com.ducanh.appchat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.ducanh.appchat.adapter.FargmentNavigationAdapter;
import com.ducanh.appchat.fragments.ChatsFragment;
import com.ducanh.appchat.fragments.UsersFragment;
import com.ducanh.appchat.model.Noti;
import com.ducanh.appchat.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private CircleImageView profileImage;
    private TextView username;

    private BottomNavigationView navigationView;
    private ViewPager viewPager;
    private FargmentNavigationAdapter adapter;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    final String CHANNEL_ID = "101";
    int notificationId=1;
    SQLiteHelper sqLiteHelper;
    List<Noti> notiList= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileImage=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                username.setText(user.getUsername());

                if (user.getImageURL().equals("default")){
                    profileImage.setImageResource(R.mipmap.ic_launcher);

                } else{
                    Glide.with(MainActivity.this).load(user.getImageURL()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ViewPager viewPager=findViewById(R.id.view_pager);
        navigationView=findViewById(R.id.navigation);

        adapter=new FargmentNavigationAdapter(getSupportFragmentManager(),
                FargmentNavigationAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_oke:viewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_chat:viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });

        //----------------------------------------------


//        try {
//            System.out.println(sqLiteHelper.getDate()+"--------------------------");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        createNotificationChannel();
        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true) {
                    try {
                        Thread.sleep(50000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (checkTime()) {


                        NotificationCompat.Builder builder =
                                new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID)
                                        .setSmallIcon(R.drawable.ic_launcher_background)
                                        .setContentTitle(notiList.get(notiList.size()-1).getTitle())
                                        .setContentText("Bài kiểm tra đang đợi bạn")
                                        .setColor(Color.RED)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setAutoCancel(true);
                        NotificationManagerCompat notificationManagerCompat =
                                NotificationManagerCompat.from(MainActivity.this);
                        notificationManagerCompat.notify(notificationId, builder.build());
                        break;
                    }

                }
            }
        }).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,StartActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            case R.id.profile:
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                finish();
                return true;
            case R.id.addSubject:
                startActivity(new Intent(MainActivity.this,AddSubjectActivity.class));
                finish();
                return true;

        }
        return false;
    }
    private  void status(String status){
        reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
    private boolean checkTime(){
        Calendar c = Calendar.getInstance();
        int hour, minute, second;
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);

        int ngay,thang,gio,phut;

        sqLiteHelper=new SQLiteHelper(this);


        try {
            notiList = sqLiteHelper.getNoti();

            if (notiList!=null) {
                Noti noti = notiList.get(notiList.size() - 1);
                String time1 = noti.getTime();
                String date[] =time1.split("/");
//            ngay = Integer.parseInt(date[2]);
//            thang = Integer.parseInt(date[3]);
                gio = Integer.parseInt(date[0]);
                phut = Integer.parseInt(date[1]);

                if (hour == gio && minute == phut &&
                        second >= 0) return true;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }



        return false;
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("This is notification channel");
            NotificationManager manager =
                    getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

}