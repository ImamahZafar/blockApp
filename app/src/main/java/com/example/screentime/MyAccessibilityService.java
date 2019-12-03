package com.example.screentime;


import android.accessibilityservice.AccessibilityService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;



public class MyAccessibilityService extends AccessibilityService {
    String TAG ="MyAccessibilityService";
     HashMap<String, String> blockList=new HashMap<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Intent intent =new Intent(this,AccessibilityService.class);
    Context context =this;
    //final Intent intent =getIntent();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        final SharedPreferences sharedPref = getBaseContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        final boolean Status=sharedPref.getBoolean("krystalizeModeStatus",false);



        final int eventType=event.getEventType();
        if(eventType==AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ) {
            Log.i(TAG, event.getPackageName().toString());
                final String userEmail = sharedPref.getString("email", null);
                final String[] temp = {new String()};
                database.getReference("User").getRef().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            if (uniqueKeySnapshot.child("email").getValue().equals(userEmail)) {
                                {
                                    temp[0] = uniqueKeySnapshot.child("blockList").getRef().getKey();
                                    for (DataSnapshot child : uniqueKeySnapshot.child("blockList").getChildren()) {
                                        temp[0] = child.getValue().toString();
                                        blockList.put(temp[0], temp[0]);
                                        Log.e("iterator: ", "=========" + blockList);
                                    }


                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        // Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

            }
                Log.i("wah", "=========" + blockList);
                if (event.getPackageName().toString().equals(blockList.get(event.getPackageName().toString()))) {

                    final SharedPreferences.Editor editor1 = sharedPref.edit();
                    editor1.putString("currentPackage", event.getPackageName().toString());
                    editor1.commit();
                    Intent myintent = new Intent(this, AlternateScreen.class);
                    myintent.putExtra("package_name", event.getPackageName().toString());
                    Log.i("ser", "=========" + event.getPackageName().toString());
                    myintent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myintent);
                }
            }


    @Override
    public void onInterrupt() {

    }


}
