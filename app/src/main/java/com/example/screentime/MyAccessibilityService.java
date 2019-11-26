package com.example.screentime;


import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.RequiresApi;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.example.screentime.MainActivity.blockList;

public class MyAccessibilityService extends AccessibilityService {
    String TAG ="MyAccessibilityService";

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType=event.getEventType();
        if(eventType==AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ){
            Log.i(TAG, event.getPackageName().toString());

//            if(event.getPackageName().toString().equals("com.google.android.youtube")){
            if(event.getPackageName().toString().equals(blockList.get(event.getPackageName().toString()))){
                Intent myintent = new Intent(this, SecondScreen.class);
                myintent.putExtra("package_name", event.getPackageName().toString());
                myintent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(myintent);
            }
        }
    }
    @Override
    public void onInterrupt() {

    }
}
