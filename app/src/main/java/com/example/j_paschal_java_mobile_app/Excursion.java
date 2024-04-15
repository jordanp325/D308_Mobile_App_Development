package com.example.j_paschal_java_mobile_app;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Entity(foreignKeys = @ForeignKey(entity = Vacation.class, parentColumns = "id", childColumns = "vacationId", onDelete = ForeignKey.RESTRICT), indices = {@Index(value = "vacationId")})
public class Excursion {
    @Setter
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long Id;

    @NonNull
    @ColumnInfo(name = "title")
    private String Title;

    @NonNull
    @ColumnInfo(name = "date")
    private long Date;

    @NonNull
    @ColumnInfo(name = "vacationId")
    private long VacationId;


    public long Id(){
        return Id;
    }

    public String Title(){
        return Title;
    }

    public long Date(){
        return Date;
    }

    public long VacationId(){
        return VacationId;
    }

    @Ignore
    public Excursion(@NonNull long id, @NonNull String Title, @NonNull long Date, @NonNull long VacationId){
        this(Title, Date, VacationId);
        Id = id;
    }

    public Excursion(@NonNull String Title, @NonNull long Date, @NonNull long VacationId){
        this.Title = Title;
        this.Date = Date;
        this.VacationId = VacationId;
    }
}
