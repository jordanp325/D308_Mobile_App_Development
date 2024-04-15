package com.example.j_paschal_java_mobile_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewVacationActivity extends AppCompatActivity {
    VacationDatabase database;
    long id = -1;
    int numExcursions = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_vacation);
        getSupportActionBar().setTitle("View Vacation");

        //this screen should never be reached without a vacation id being passed in
        id = getIntent().getLongExtra("id", -1);
        database = VacationDatabase.getDatabase(getApplicationContext());

        Vacation vacation = database.vacationDao().getVacation(id);
        ((TextView)findViewById(R.id.viewVacationTitle)).setText(vacation.Title());
        ((TextView)findViewById(R.id.viewVacationLocation)).setText(vacation.PlaceOfStay());
        ((TextView)findViewById(R.id.viewVacationStartDate)).setText(AddVacationActivity.DateToShortString(vacation.StartDate()));
        ((TextView)findViewById(R.id.viewVacationEndDate)).setText(AddVacationActivity.DateToShortString(vacation.EndDate()));
        ((CheckBox)findViewById(R.id.viewVacationNotify)).setChecked(vacation.Notify());

        List<Excursion> excursions = database.excursionDao().getExcursions(id);
        numExcursions = excursions.size();
        if(excursions.size() > 0) {
            for (Excursion e : excursions) {
                AddExcursion(e.Id());
            }
        }
        else{
            noExcursions();
        }
    }


    public void ClickHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void ClickEdit(View view){
        Intent intent = new Intent(this, AddVacationActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void ClickDelete(View view){
        database.vacationDao().deleteVacation(database.vacationDao().getVacation(id));
        ClickHome(view);
    }

    public void ClickNotify(View view){
        Vacation vacation = database.vacationDao().getVacation(id);
        vacation.SetNotification(((CheckBox)findViewById(R.id.viewVacationNotify)).isChecked());
        database.vacationDao().updateVacation(vacation);
    }

    public void ClickShare(View view){
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, MainActivity.ExportVacation(database.vacationDao().getVacation(id)));
        share.setType("text/plain");
        startActivity(share);
    }



    public void ClickAddExcursion(View v){
        Intent intent = new Intent(this, EditExcursionActivity.class);
        intent.putExtra("vacationId", id);
        startActivity(intent);
    }

    void AddExcursion(long eId){
        Locale locale = new Locale.Builder().setLanguage("en").setRegion("US").build();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        Excursion e = database.excursionDao().getExcursion(eId);
        //this variable must be final for use in the below classes
        final long excursionId = eId;
        ViewVacationActivity activity = this;

        View excursion = MainActivity.LI.inflate(R.layout.excursion_entry, null);
        ((TextView)excursion.findViewById(R.id.excursionEntryTitle)).setText(e.Title() + " - "+ dateFormat.format(new Date(e.Date())));
        ((Button)excursion.findViewById(R.id.excursionEntryEdit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EditExcursionActivity.class);
                intent.putExtra("vacationId", id);
                intent.putExtra("excursionId", excursionId);
                startActivity(intent);
            }
        });
        ((Button)excursion.findViewById(R.id.excursionEntryDelete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.excursionDao().deleteExcursion(database.excursionDao().getExcursion(excursionId));
                ((ViewGroup)findViewById(R.id.viewVacationExcursions)).removeView(excursion);

                numExcursions--;
                if(numExcursions == 0)
                    noExcursions();
            }
        });
        ((Button)excursion.findViewById(R.id.excursionEntryView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ViewExcursionActivity.class);
                intent.putExtra("vacationId", id);
                intent.putExtra("excursionId", excursionId);
                startActivity(intent);
            }
        });

        ((ViewGroup)findViewById(R.id.viewVacationExcursions)).addView(excursion);

        if(AddVacationActivity.DateToShortString(e.Date()).equals(AddVacationActivity.DateToShortString(new Date().getTime())) && e.Notify()){
            AddVacationActivity.DisplayPopup(this, "Time for "+e.Title()+"!");
        }
    }

    void noExcursions(){
        View noExcursions = MainActivity.LI.inflate(R.layout.no_excursions, null);
        ((ViewGroup)findViewById(R.id.viewVacationExcursions)).addView(noExcursions);
    }
}
