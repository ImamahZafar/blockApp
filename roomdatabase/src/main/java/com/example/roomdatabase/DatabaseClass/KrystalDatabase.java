package com.example.roomdatabase.DatabaseClass;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.roomdatabase.Dao.DaoAccess;
import com.example.roomdatabase.Krystal;

public class KrystalDatabase {
    @Database(entities = {Krystal.class}, version = 1, exportSchema = false)
    public abstract class DatabaseClass extends RoomDatabase {

        public abstract DaoAccess daoAccess();
    }
}
