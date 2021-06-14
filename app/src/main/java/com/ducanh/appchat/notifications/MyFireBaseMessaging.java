package com.ducanh.appchat.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ducanh.appchat.MessageActivity;
import com.ducanh.appchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessaging extends FirebaseMessagingService {

    private NotificationManagerCompat
            notificationManagerCompat;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sented=remoteMessage.getData().get("sented");
        String user=remoteMessage.getData().get("user");

        SharedPreferences preferences=getSharedPreferences("PRES",MODE_PRIVATE);
        String currenUser=preferences.getString("currentuser","none");

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null && sented.equals(firebaseUser.getUid())) {
            if (!currenUser.equals(user)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOreoNotifiaction(remoteMessage);
                }
            }
        }
    }
    private void sendOreoNotifiaction(RemoteMessage remoteMessage){
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification=remoteMessage.getNotification();
//        String s=user.replaceAll("[\\D]","");
//        System.out.println("++++" + user.replaceAll("[\\D]", "") + "++++++++++++++++++++++++");
        int j = 5;
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaulSound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        OreoNotification oreoNotification=new OreoNotification(this);
        Notification.Builder builder=oreoNotification.getOreoNotification(title,body,pendingIntent,defaulSound,icon);

        int i = 0;
        if (j > 0) {
            i = j;
        }
        oreoNotification.getManager().notify(i, builder.build());
    }
}
