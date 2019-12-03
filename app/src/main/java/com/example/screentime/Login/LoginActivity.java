package com.example.screentime.Login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.screentime.R;
import com.example.screentime.SecondScreen;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private Button SignInbutton;
    private EditText email;
    private EditText password;
    private TextView check;
    private LoginPresenter presenter;
    int count1=0;
    String currentPackage;
    Boolean blackListStatus=false;
    Boolean hibernateStatus=false;
    Boolean krystalizeModeStatus=false;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        SignInbutton = (Button) findViewById(R.id.button2);
        SignInbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCredentials();

            }
        });
        presenter = new LoginPresenter(this, new LoginInteractor());
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }



    @Override
    public void setUsernameError() {
        email.setError(getString(R.string.email_error));
    }

    @Override
    public void setPasswordError() {
        password.setError(getString(R.string.password_error));
    }

    @Override
    public void navigateToHome() {

                        Context context = LoginActivity.this;
                        final SharedPreferences sharedPref = context.getSharedPreferences(
                                "loginInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("email", email.getText().toString());
                        editor.putString("password", password.getText().toString());
                        editor.putBoolean("blacklistStatus",blackListStatus.booleanValue());
                        editor.putBoolean("hibernateStatus",hibernateStatus.booleanValue());
                        editor.putBoolean("krystalizeModeStatus",krystalizeModeStatus.booleanValue());
                        editor.putInt("Krystal1",count1);
                        editor.putInt("Krystal2",count1);
                        editor.putString("currentPackage",currentPackage);
                        editor.apply();
                        Toast.makeText(this, "Successful login", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, SecondScreen.class));
                        finish();
                    }


    @Override
    public void setLoginError(){
        Toast.makeText(this,"Wrong username or password",Toast.LENGTH_SHORT).show();
    }

    private void validateCredentials() {
        presenter.validateCredentials(email.getText().toString(), password.getText().toString());
    }
}