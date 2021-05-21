package com.ducanh.appchat.fragments;

import com.ducanh.appchat.notifications.MyResponse;
import com.ducanh.appchat.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAS0eRJx4:APA91bFJVVQ1LRuZ2c3ZEnhhyB7k7hQrMDnDu0UzTncHj2Bo_Zmyze0rAoDoATWo3I7YoJB1a_J7L3aRyjTya43GRNB_N0erzTpGGjsST4XP-p435NoeaBXNU_8RfaNf2F__1haHW-Qr"
            }

    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
