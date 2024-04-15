package com.example.j_paschal_java_mobile_app;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//if database scheme is out of date, increase this version number and the database will be rebuilt
@Database(entities = {Vacation.class, Excursion.class}, version = 4, exportSchema = false)
public abstract class VacationDatabase extends RoomDatabase {
    public abstract VacationDao vacationDao();
    public abstract ExcursionDao excursionDao();

    private static VacationDatabase INSTANCE;
    public static VacationDatabase getDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, VacationDatabase.class, "vacation.db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
}
