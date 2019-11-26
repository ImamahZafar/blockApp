package com.example.screentime;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondScreen extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondscreen);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            packageName = extras.getString("package_name");
            Log.d("SecondScreen", "Package: "+packageName);

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
    }
}
