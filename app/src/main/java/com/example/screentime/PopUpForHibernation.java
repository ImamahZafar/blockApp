package com.example.screentime;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopUpForHibernation extends AppCompatActivity {
    private Button hibernateForFiveMin;
    private Button hibernateForThirtyMin;
    private Button hibernateForOneHour;
    private Button hibernateForTwoHour;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up);
        final Intent intent = getIntent();
        DisplayMetrics dm =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width =dm.widthPixels;
        int height=dm.heightPixels;

        final SharedPreferences sharedPref = this.getSharedPreferences("HibernationInfo", Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = sharedPref.edit();
        getWindow().setLayout((int)(width*.8),(int)(height*.6));
        final SharedPreferences sharedPref1 = getBaseContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        final SharedPreferences.Editor loginInfoeditor =sharedPref1.edit();
        final String userEmail = sharedPref1.getString("email", null);
        hibernateForFiveMin=findViewById(R.id.min5);
        hibernateForThirtyMin=findViewById(R.id.min30);
        hibernateForOneHour=findViewById(R.id.hour1);
        hibernateForTwoHour=findViewById(R.id.hour2);

        hibernateForFiveMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PopUpForHibernation.this, "App is in hibernation mode for 5 minutes", Toast.LENGTH_SHORT).show();
                int count1 = sharedPref1.getInt("Krystal1", 0);

                count1 = count1 + 1;
                loginInfoeditor.putInt("Krystal1", count1);
                editor.putInt("time", 5);
                editor.commit();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void run() {
                        //Do something after 5m

                        editor.putInt("time",5);
                        editor.commit();
                        showNotification(PopUpForHibernation.this,"Yayyy!","You acquired a new Krystal",intent);


                    }

                        }, 100000);
                    }
                });


        hibernateForThirtyMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PopUpForHibernation.this,"App is in hibernation mode for 30 minutes", Toast.LENGTH_SHORT).show();
                editor.putInt("time",30);
                editor.commit();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 5m
                        final String appName=sharedPref.getString("appName",null);
                        database.getReference("User").getRef().addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                                    if (uniqueKeySnapshot.child("email").getValue().equals(userEmail)) {
                                        {

                                            for (DataSnapshot child: uniqueKeySnapshot.child("blockList").getChildren()){
                                                if(appName.equals(child.getValue().toString()))
                                                {

                                                    child.getRef().setValue(null);
                                                    showNotification(PopUpForHibernation.this,"Yayyy!","You acquired a new Krystal",intent);

                                                }

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
                }, 18000000);
            }
        });



hibernateForOneHour.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(PopUpForHibernation.this,"App is in hibernation mode for an hour", Toast.LENGTH_SHORT).show();
        editor.putInt("time",60);
        editor.commit();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 1hour
                final String appName=sharedPref.getString("appName",null);
                database.getReference("User").getRef().addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            if (uniqueKeySnapshot.child("email").getValue().equals(userEmail)) {
                                {

                                    for (DataSnapshot child: uniqueKeySnapshot.child("blockList").getChildren()){
                                        if(appName.equals(child.getValue().toString()))
                                        {

                                            child.getRef().setValue(null);
                                            showNotification(PopUpForHibernation.this,"Yayyy!","You acquired a new Krystal",intent);

                                        }

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
        }, 3600000);
    }
});

hibernateForTwoHour.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(PopUpForHibernation.this,"App is in hibernation mode for 2 hour", Toast.LENGTH_SHORT).show();
        editor.putInt("time",120);
        editor.commit();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 1hour
                final String appName=sharedPref.getString("appName",null);
                database.getReference("User").getRef().addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            if (uniqueKeySnapshot.child("email").getValue().equals(userEmail)) {
                                {

                                    for (DataSnapshot child: uniqueKeySnapshot.child("blockList").getChildren()){
                                        if(appName.equals(child.getValue().toString()))
                                        {

                                            child.getRef().setValue(null);
                                            showNotification(PopUpForHibernation.this,"Yayyy!","You acquired a new Krystal",intent);

                                        }

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
        }, 7200000);
    }
});

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

}
