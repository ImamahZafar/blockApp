package com.example.screentime;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.FirebaseDatabase;

public class DisplayStatistics extends Fragment {

    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;
    TextView text5;
    TextView text6;
    String userName;
    String packageName;
    int i = 0;
    String appname[] = new String[3];

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistics, container, false);
        text1 = view.findViewById(R.id.text1);
        text2 = view.findViewById(R.id.text2);
        text3 = view.findViewById(R.id.text3);
        text4= view.findViewById(R.id.text4);
        text5 = view.findViewById(R.id.text5);
        text6 = view.findViewById(R.id.text6);
        Bundle extras = getActivity().getIntent().getExtras();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        final int count1 = sharedPref.getInt("Krystal1", 0);
        text1.setText(String.valueOf(count1));


            return inflater.inflate(R.layout.statistics, container, false);
        }
    }

