package com.example.screentime;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class DoNotDisturb extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.do_not_disturb);
        final SharedPreferences sharedPref = getBaseContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        final boolean Status=sharedPref.getBoolean("krystalizeModeStatus",false);
        if(Status==false) {
            finish();
            Log.i("noti", "========= noti" + Status);
        }
        else
            Log.i("notii", "========= " + Status);

    }
}
