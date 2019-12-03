package com.example.screentime;


import android.accessibilityservice.AccessibilityService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.screentime.Adapter.AppAdapter;
import com.example.screentime.Model.AppInfo;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class displayAppsScreen extends AppCompatActivity {

    ListView listView;
    public String temp=new String();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    static AccessibilityService accessibilityService;
    public static HashMap<String, String> blockList=new HashMap<>();

    boolean mIncludeSystemApps;
   public static String blockedApp=new String();
    String key;
    private static displayAppsScreen context;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_apps);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final SharedPreferences sharedPref1 = this.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);

        context=this;
        //final ScreenTimeRepo screenTimeRepo=new ScreenTimeRepo(context);
        final String uid = FirebaseDatabase.getInstance().getReference("User").getKey();
        listView = (ListView) findViewById(R.id.listview);
        mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefresh);


        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo item = (AppInfo) listView.getItemAtPosition(position);
                final String packageName=item.packageName;
                if (sharedPref1.getBoolean("krystalizeModeStatus", false) == true) {
                    setPackageNameForSchedule(packageName);
                }
                else {
                    setpackageName(packageName);
                }
                blockedApp=packageName;
                //blockList=getBlockList();
                Log.i("q", "========="+packageName);

                //blockList.put(temp,temp);
                Toast.makeText(displayAppsScreen.this,packageName+" has been blocked", Toast.LENGTH_SHORT).show();

            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshIt();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAppInfoTask loadAppIntoTask=new loadAppInfoTask();
        loadAppIntoTask.execute(PackageManager.GET_META_DATA);

    }

    private void refreshIt()
    {
        loadAppInfoTask loadAppIntoTask=new loadAppInfoTask();
        loadAppIntoTask.execute(PackageManager.GET_META_DATA);

    }




    class loadAppInfoTask extends AsyncTask<Integer,Integer, List<AppInfo>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
        }


        @Override
        protected List<AppInfo> doInBackground(Integer... params) {

            List<AppInfo> apps = new ArrayList<>();
            PackageManager pm=getPackageManager();

            List<ApplicationInfo> infos = pm.getInstalledApplications(params[0]);
            for(ApplicationInfo info : infos )
            {
                if(!mIncludeSystemApps && (info.flags & ApplicationInfo.FLAG_SYSTEM)==1)
                {
                    continue;
                }
                AppInfo app =new AppInfo();
                app.info =info;
                app.label =(String)info.loadLabel(pm);
                app.packageName=(String)info.packageName;
                apps.add(app);


            }

            return apps;
        }



        @Override
        protected void onPostExecute(List<AppInfo> appInfos) {
            super.onPostExecute(appInfos);
            listView.setAdapter(new AppAdapter(displayAppsScreen.this,appInfos));
            mSwipeRefreshLayout.setRefreshing(false);
            Snackbar.make(listView,appInfos.size()+" application loaded ",Snackbar.LENGTH_LONG).show();
        }
    }

    void setpackageName(final String packageNameToBeAdded){
            final int[] i = {0};
            final SharedPreferences sharedPref = this.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
            final String userEmail = sharedPref.getString("email", null);
            database.getReference("User").getRef().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                        if (uniqueKeySnapshot.child("email").getValue().equals(userEmail)) {
                            if (i[0] == 0) {

                                uniqueKeySnapshot.getRef().child("blockList").push().setValue(packageNameToBeAdded);
                                i[0] = 1;
                            }

                            break;
                        }


                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    // Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

            if (sharedPref.getBoolean("hibernateStatus", false) == true) {
                final SharedPreferences sharedPrefForHibernation = context.getSharedPreferences(
                        "HibernationInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefForHibernation.edit();
                editor.putString("appName", packageNameToBeAdded);
                editor.putInt("time", 0);
                editor.apply();
                Intent intent = new Intent(this, PopUpForHibernation.class);
                startActivity(intent);

            } else if (sharedPref.getBoolean("hibernateStatus", false) == false) {

            }
        }

       @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
       void setPackageNameForSchedule(final String packageNameToBeAdded) {
           final Intent intent = getIntent();
            final Handler handler = new Handler();
           showNotification(displayAppsScreen.this, "Alert", "Your app is scheduled to be blocked in 30 min", intent);
            final SharedPreferences sharedPref1 = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final int[] i = {0};

                    final String userEmail = sharedPref1.getString("email", null);
                    database.getReference("User").getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                                if (uniqueKeySnapshot.child("email").getValue().equals(userEmail)) {
                                    if (i[0] == 0) {

                                        uniqueKeySnapshot.getRef().child("blockList").push().setValue(packageNameToBeAdded);
                                        i[0] = 1;
                                    }

                                    break;
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

            }, 10000);
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




