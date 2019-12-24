package com.example.screentime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;
public class SignUp extends AppCompatActivity {
    private Button SignUpbutton;
    EditText email;
    EditText password;
    EditText confirmpass;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SignUpbutton = findViewById(R.id.signUpbutton);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        confirmpass = findViewById(R.id.confirmPass);
        final int krystal1Count=0;
        final int krystal2Count=0;
        final int krystal3Count=0;
        final int krystal4Count=0;
        final int krystal5Count=0;
        final int krystal6Count=0;
        final int krystalCount = 0;
        final String appname = "default name";
        final int balance = 0;
        final String blockList = new String();


        SignUpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmail(email) == false) {
                    email.setError("Enter valid email");
                } else if (isEmpty(name)) {
                    name.setError("Enter name");
                } else if (isEmpty(password)) {
                    password.setError("Password is required");
                } else if (!password.getText().toString().equals(confirmpass.getText().toString())) {
                    confirmpass.setError("Password does not match");
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", name.getText().toString());
                    map.put("password", password.getText().toString());
                    map.put("email", email.getText().toString());
                    map.put("krystalCount", krystalCount);
                    map.put("balance", balance);
                    map.put("blockList", blockList);
                    map.put("Krystal1",krystal1Count);
                    map.put("Krystal2",krystal2Count);
                    map.put("Krystal3",krystal3Count);
                    map.put("Krystal4",krystal4Count);
                    map.put("Krystal5",krystal5Count);
                    map.put("Krystal6",krystal6Count);


                    FirebaseDatabase.getInstance().getReference().child("User").push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast t = Toast.makeText(SignUp.this, "data inserted in database", LENGTH_SHORT);
                            t.show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                    final SharedPreferences sharedPref = SignUp.this.getSharedPreferences(
                            "loginInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("email", email.getText().toString());
                    editor.putString("password", password.getText().toString());
                    editor.apply();
                    openSecondScreen();
                }
            }
        });

    }

    public void openSecondScreen() {
        Intent intent = new Intent(this, SecondScreen.class);
        startActivity(intent);
        finish();
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isPass(EditText text) {
        CharSequence password = text.getText().toString();
        return (!TextUtils.isEmpty(password));
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void checkDataEntered() {
        if (isEmail(email) == false) {
            email.setError("Enter valid email");
        }

        if (isEmpty(name)) {
            Toast t = Toast.makeText(this, "You must enter name ", LENGTH_SHORT);
            t.show();
        }
        if (isEmpty(password)) {
            password.setError("Password is required");
        }
    }

    public void toExit(View view) {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}