package com.example.j_paschal_java_mobile_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.EndElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static LayoutInflater LI;
    VacationDatabase database;
    int vacationsNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Starting...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Vacation Scheduler");

        database = VacationDatabase.getDatabase(getApplicationContext());
//        database.excursionDao().deleteAll();
//        database.vacationDao().deleteAll();

        LI = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        List<Vacation> vacations = database.vacationDao().getVacations();
        if(vacations.size() > 0) {
            for (Vacation v : vacations) {
                AddEntry(v);
                if(AddVacationActivity.DateToShortString(v.StartDate()).equals(AddVacationActivity.DateToShortString(new Date().getTime())) && v.Notify()){
                    AddVacationActivity.DisplayPopup(this, "Time for "+v.Title()+"!\n Let's go on vacation!");
                }
                if(AddVacationActivity.DateToShortString(v.EndDate()).equals(AddVacationActivity.DateToShortString(new Date().getTime())) && v.Notify()){
                    AddVacationActivity.DisplayPopup(this, "It's the last day of "+v.Title());
                }
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
                if(database.excursionDao().getExcursions(id).size() > 0){
                    AddVacationActivity.DisplayPopup(activity, "You can't delete a vacation while it still has excursions");
                    return;
                }

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

        ((Button)vacation.findViewById(R.id.vacationView)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ViewVacationActivity.class);
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

    public static String ExportVacation(Vacation vacation){
        //create string array of data
        String[] strings = new String[]{vacation.Title(), vacation.PlaceOfStay(), AddVacationActivity.DateToShortString(vacation.StartDate()), AddVacationActivity.DateToShortString(vacation.EndDate())};

        //escape each special character
        //$ - escape character
        //~ - seperator for data string
        for(int i = 0; i < strings.length; i++){
            strings[i].replaceAll("\\$", "$$");
            strings[i].replaceAll("~", "$~");
        }

        //combine strings with seperator
        String combined = strings[0];
        for(int i = 1; i < strings.length; i++){
            combined += "~" + strings[i];
        }

        //encode string data
        String encodedString = Base64.getEncoder().encodeToString(combined.getBytes());

        //return encoded data
        return encodedString;
    }

    public static Vacation ImportVacation(String code) {
        return ImportVacation(code, -1);
    }
    public static Vacation ImportVacation(String code, long id){
        //decode from string
        String decodedString = new String(Base64.getDecoder().decode(code));

        //separate strings with some clever regex
        //Java will not allow a * or {0,} inside of a regex lookbehind, so I have to add a finite limit to the number of $ escapes
        //This means if anyone adds 10001 or more $ at the end of their vacation title or place of stay, the vacation code will be unreadable
        String[] strings = decodedString.split("(?<=[^\\$](\\$\\$){0,10000})~");

        //unescape all escaped characters
        for(int i = 0; i < strings.length; i++){
            strings[i].replaceAll("\\$~", "~");
            strings[i].replaceAll("\\$\\$", "$");
        }

        //recompile all string data into vacation object
        Vacation vacation;
        if(id == -1) vacation = new Vacation(strings[0], strings[1], new Date(strings[2]).getTime(), new Date(strings[3]).getTime());
        else vacation = new Vacation(id, strings[0], strings[1], new Date(strings[2]).getTime(), new Date(strings[3]).getTime());

        //return vacation object
        return vacation;
    }
}