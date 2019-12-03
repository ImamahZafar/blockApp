package com.example.screentime.Login;

import android.os.Handler;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginInteractor {
    String checkEmail;
    String checkPassword;
    //private DatabaseReference Post= FirebaseDatabase.getInstance().getReference().child("User");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference Post= database.getReference();
    interface OnLoginFinishedListener {
        void onUsernameError();
        void onPasswordError();
        void onSuccess();
        void onWrongLogin();
    }

    public void login(final String username, final String password, final OnLoginFinishedListener listener) {
        // Mock login. I'm creating a handler to delay the answer a couple of seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(username)) {
                    listener.onUsernameError();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    listener.onPasswordError();
                    return;
                }

                database.getReference("User").getRef().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            if(uniqueKeySnapshot.child("email").getValue().equals(username) && uniqueKeySnapshot.child("email").getValue()!=null) {
                                {
                                    if(uniqueKeySnapshot.child("password").getValue().equals(password))
                                    {
                                        listener.onSuccess();
                                        return;
                                    }

                                }

                            }
                            else
                                listener.onWrongLogin();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        // Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });




            }
        }, 2000);
    }


}
