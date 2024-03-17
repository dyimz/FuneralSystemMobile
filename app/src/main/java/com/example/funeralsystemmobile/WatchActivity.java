package com.example.funeralsystemmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingConfig;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingFragment;

public class WatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        addFragment();
    }
    public void addFragment() {
        Integer custid =  Integer.parseInt(getIdFromSharedPreferences());
        String name = getNameFromSharedPreferences();


        long appID = 1121715831;
        String appSign = "f6e35761b8fe8730dfe4db678ee3ac203068ef209264c64bf1dc24c31cbc9580";
        String userID = String.valueOf(custid);
        String userName = name;

        boolean isHost = false;
        String liveID = "PikesFuneralLive";

        ZegoUIKitPrebuiltLiveStreamingConfig config;
        if (isHost) {
            config = ZegoUIKitPrebuiltLiveStreamingConfig.host();
        } else {
            config = ZegoUIKitPrebuiltLiveStreamingConfig.audience();
        }
        ZegoUIKitPrebuiltLiveStreamingFragment fragment = ZegoUIKitPrebuiltLiveStreamingFragment.newInstance(
                appID, appSign, userID, userName,liveID,config);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
    }

    private String getIdFromSharedPreferences() {
        // Retrieve the id from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("custid", "");
    }

    private String getNameFromSharedPreferences() {
        // Retrieve the id from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("name", "");
    }
}