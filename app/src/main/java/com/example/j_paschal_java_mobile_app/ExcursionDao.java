package com.example.j_paschal_java_mobile_app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExcursionDao {
    @Query("SELECT * FROM Excursion WHERE vacationId = :vacationId")
    List<Excursion> getExcursions(long vacationId);

    @Query("SELECT * FROM Excursion WHERE id = :id")
    Excursion getExcursion(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addExcursion(Excursion excursion);

    @Update
    void updateExcursion(Excursion excursion);

    @Delete
    void deleteExcursion(Excursion excursion);

    @Query("DELETE FROM Excursion")
    void deleteAll();
}
