package com.example.roomdatabase.repository;

import android.content.Context;

import com.example.roomdatabase.DatabaseClass.KrystalDatabase;

public class KrystalRepo {
    private String DB_NAME = "app_core_db";

    private KrystalDatabase krystalDatabase;

    public KrystalRepo(Context context) {
        getInstance(context);
    }

    private  KrystalDatabase getInstance(Context context){
        if (krystalDatabase!=null){
            return krystalDatabase;
        }
        else {
           // krystalDatabase = databaseBuilder(context,KrystalDatabase.class,DB_NAME).build();
        }
        return krystalDatabase;
    }

}
