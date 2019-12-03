package com.example.screentime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.screentime.Login.LoginActivity;


public class MainActivity extends AppCompatActivity {
    private Button SignUpbutton;
    private Button SignInbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignInbutton = (Button) findViewById(R.id.signIn);
        SignInbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignInScreen();
            }
        });

        SignUpbutton = (Button) findViewById(R.id.signUp);
        SignUpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpScreen();
            }
        });
    }




    public void toExit(View view)
    {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public void openSignInScreen()
    {
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void openSignUpScreen()
    {
        Intent intent=new Intent(this,SignUp.class);
        startActivity(intent);
        finish();
    }


}
