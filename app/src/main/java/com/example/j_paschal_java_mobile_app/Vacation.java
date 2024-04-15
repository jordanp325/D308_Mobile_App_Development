package com.example.j_paschal_java_mobile_app;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.Setter;

@Entity(indices = {@Index(value = "id")})
public class Vacation {
    @Setter
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long Id;

    @NonNull
    @ColumnInfo(name = "title")
    private String Title;

    @NonNull
    @ColumnInfo(name = "placeOfStay")
    private String PlaceOfStay;

    @NonNull
    @ColumnInfo(name = "startDate")
    private long StartDate;

    @NonNull
    @ColumnInfo(name = "endDate")
    private long EndDate;

    @NonNull
    @Setter
    @ColumnInfo(name = "notify")
    private boolean Notify = false;


    public long Id(){
        return Id;
    }

    public String Title(){
        return Title;
    }

    public String PlaceOfStay(){
        return PlaceOfStay;
    }

    public long StartDate(){
        return StartDate;
    }

    public long EndDate(){
        return EndDate;
    }

    public boolean Notify(){return Notify;}
    public void SetNotification(boolean notify){Notify = notify;}

    @Ignore
    public Vacation(@NonNull long Id, @NonNull String Title, @NonNull String PlaceOfStay, @NonNull long StartDate, @NonNull long EndDate)  {
        this(Title, PlaceOfStay, StartDate, EndDate);
        this.Id = Id;
    }

    public Vacation(@NonNull String Title, @NonNull String PlaceOfStay, @NonNull long StartDate, @NonNull long EndDate) {
        this.Title = Title;
        this.PlaceOfStay = PlaceOfStay;
        this.StartDate = StartDate;
        this.EndDate = EndDate;
    }
}
