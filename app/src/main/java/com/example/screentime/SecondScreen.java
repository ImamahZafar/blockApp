package com.example.screentime;
import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;

public class SecondScreen extends AppCompatActivity implements Tab1.OnFragmentInteractionListener, Tab2.OnFragmentInteractionListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Bundle savedInstanceState;
    @SuppressLint("WrongViewCast")
    private static final String LOGTAG = "MainActivity";

    static AccessibilityService accessibilityService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);


        boolean enabled = isAccessibilityServiceEnabled(getApplicationContext(), AccessibilityService.class);
        if(!enabled) {
            Intent openSettings = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            //openSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(openSettings);
        }else{
            Toast.makeText(getApplicationContext(),"Accessibility service is already on",Toast.LENGTH_SHORT).show();
        }

        TabLayout tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tab1));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tab2));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager=(ViewPager)findViewById(R.id.pager);
        final pagerAdapter adapter=new pagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public static boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> service) {
        ComponentName expectedComponentName = new ComponentName(context, String.valueOf(accessibilityService));

        String enabledServicesSetting = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabledServicesSetting == null)
            return false;

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
        colonSplitter.setString(enabledServicesSetting);

        while (colonSplitter.hasNext()) {
            String componentNameString = colonSplitter.next();
            ComponentName enabledService = ComponentName.unflattenFromString(componentNameString);

            if (enabledService != null && enabledService.equals(expectedComponentName))
                return true;
        }

        return false;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

