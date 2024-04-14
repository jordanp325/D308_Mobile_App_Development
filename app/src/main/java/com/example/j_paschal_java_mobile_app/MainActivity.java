package com.example.j_paschal_java_mobile_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    VacationDatabase database;
    LayoutInflater LI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Starting...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = VacationDatabase.getDatabase(getApplicationContext());
//        database.excursionDao().deleteAll();
//        database.vacationDao().deleteAll();

        LI = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Vacation v : database.vacationDao().getVacations()){
            AddEntry(v);
        }
    }


    public void CreateVacationOnClick(View view){
        EditText text = (EditText)findViewById(R.id.vacationTitleTB);
        Vacation v = new Vacation(text.getText().toString(), "", new Date().getTime(), new Date().getTime());
        database.vacationDao().addVacation(v);
        AddEntry(v);
    }

    void AddEntry(Vacation v){
        View vacation = LI.inflate(R.layout.vacation_entry, null);
        TextView textView = (TextView)vacation.findViewById(R.id.vacationEntryName);
        textView.setText(v.Title());

        //shallow id reference for later
        long id = v.Id();

        ((Button)vacation.findViewById(R.id.buttonDelete)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Vacation v = database.vacationDao().getVacation(id);
                database.vacationDao().deleteVacation(v);
                ((ViewGroup)findViewById(R.id.insertPoint)).removeView(vacation);
            }
        });

        ((Button)vacation.findViewById(R.id.buttonReplace)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String newTitle = ((EditText)findViewById(R.id.vacationTitleTB)).getText().toString();
                Vacation prev = database.vacationDao().getVacation(id);

                Vacation v = new Vacation(id, newTitle, prev.PlaceOfStay(), prev.StartDate(), prev.EndDate());
                database.vacationDao().updateVacation(v);
                textView.setText(newTitle);
            }
        });

        ((ViewGroup)findViewById(R.id.insertPoint)).addView(vacation);
    }
}