package com.example.j_paschal_java_mobile_app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VacationDao {
    @Query("SELECT * FROM Vacation ORDER BY startDate")
    List<Vacation> getVacations();

    @Query("SELECT * FROM Vacation WHERE id = :id")
    Vacation getVacation(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addVacation(Vacation vacation);

    @Update
    void updateVacation(Vacation vacation);

    @Delete
    void deleteVacation(Vacation vacation);

    @Query("DELETE FROM Vacation")
    void deleteAll();
}
