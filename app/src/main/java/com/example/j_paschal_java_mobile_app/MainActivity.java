package com.example.j_paschal_java_mobile_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    VacationDatabase database;
    LayoutInflater LI;
    int vacationsNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Starting...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = VacationDatabase.getDatabase(getApplicationContext());
//        database.excursionDao().deleteAll();
//        database.vacationDao().deleteAll();

        LI = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        List<Vacation> vacations = database.vacationDao().getVacations();
        if(vacations.size() > 0) {
            for (Vacation v : vacations) {
                AddEntry(v);
            }
        }
        else{
            displayNoVacation();
        }
    }

    void AddEntry(Vacation v){
        Locale locale = new Locale.Builder().setLanguage("en").setRegion("US").build();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

        View vacation = LI.inflate(R.layout.vacation_entry, null);
        ((TextView)vacation.findViewById(R.id.vacationEntryName)).setText(v.Title());
        ((TextView)vacation.findViewById(R.id.vacationEntryLocation)).setText("at "+v.PlaceOfStay());
        ((TextView)vacation.findViewById(R.id.vacationEntryDates)).setText(dateFormat.format(new Date(v.StartDate())) + " - " + dateFormat.format(new Date(v.EndDate())));

        //shallow id reference for later
        long id = v.Id();
        MainActivity activity = this;

        ((Button)vacation.findViewById(R.id.vacationDelete)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Vacation v = database.vacationDao().getVacation(id);
                database.vacationDao().deleteVacation(v);
                ((ViewGroup)findViewById(R.id.insertPoint)).removeView(vacation);

                vacationsNumber--;
                if(vacationsNumber == 0)
                    displayNoVacation();
            }
        });

        ((Button)vacation.findViewById(R.id.vacationEdit)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AddVacationActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        ((ViewGroup)findViewById(R.id.insertPoint)).addView(vacation);
        vacationsNumber++;
    }

    public void AddVacation(View view){
        Intent intent = new Intent(this, AddVacationActivity.class);
        startActivity(intent);
    }

    void displayNoVacation(){
        View noVacation = LI.inflate(R.layout.no_vacation, null);
        ((ViewGroup)findViewById(R.id.insertPoint)).addView(noVacation);
    }
}