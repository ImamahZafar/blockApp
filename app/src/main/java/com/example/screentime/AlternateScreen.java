package com.example.screentime;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class AlternateScreen extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    TextView timer;
    String packageName;
    Button unblock;
    int seconds = 60;
    int minute = 00;
    int hour = 1;
    CountDownTimer t;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondscreen);
        imageView = findViewById(R.id.imageView);
        timer = findViewById(R.id.time);
        unblock = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        final SharedPreferences sharedPref = this.getSharedPreferences("HibernationInfo", Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = sharedPref.edit();
        final Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        final Timer t = new Timer("hello", true);
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                textView.post(new Runnable() {

                    public void run() {
                        seconds--;
                        if (seconds == 00) {
                            seconds = 59;
                            minute--;
                        }
                        if (minute == 00) {
                            minute = 59;
                            hour--;
                        }


                    }
                });

            }
        }, 1000, 1000);


        if (extras != null) {
            packageName = extras.getString("package_name");
            Log.d("AlternateScreen", "Package: " + packageName);

            Drawable icon = null;
            try {
                icon = getPackageManager().getApplicationIcon(packageName);
                PackageManager packageManager = getPackageManager();
                ApplicationInfo info = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                String appName = (String) packageManager.getApplicationLabel(info);
                textView.setText(appName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            imageView.setImageDrawable(icon);
        }
        unblock.setOnClickListener(new View.OnClickListener() {
            SharedPreferences sharedPref1 = getBaseContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
            final String userEmail = sharedPref1.getString("email", null);

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                final String appName = sharedPref1.getString("currentPackage", null);
                Context context = AlternateScreen.this;
                final SharedPreferences unblockAppName = context.getSharedPreferences(
                        "unblockApp", Context.MODE_PRIVATE);
                SharedPreferences.Editor unblockEditor = unblockAppName.edit();
                unblockEditor.putString("unblockAppName",appName);
                unblockEditor.apply();
                database.getReference("User").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            if (uniqueKeySnapshot.child("email").getValue().equals(userEmail)) {
                                {
                                    for (DataSnapshot child : uniqueKeySnapshot.child("blockList").getChildren()) {
                                        if (appName.equals(child.getValue().toString())) {
                                            // child.getRef().setValue(null);
                                            child.getRef().removeValue();

                                            showNotification(AlternateScreen.this, "Oh No!", "You lost a Krystal", intent);
                                         finish();
                                            break;

                                        }
                                        else
                                            Log.e("error: ", "=========errorrr");

                                    }


                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });
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