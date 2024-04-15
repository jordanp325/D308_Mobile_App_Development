package com.example.j_paschal_java_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.List;

public class ViewVacationActivity extends AppCompatActivity {
    VacationDatabase database;
    long id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_vacation);

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
        for (Excursion e : excursions){
            AddExcursion(e.Id());
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
        AddExcursion();
    }

    void AddExcursion(){
        AddExcursion(-1);
    }

    void AddExcursion(long eId){
        String title;
        if(eId == -1){
            title = ((EditText)findViewById(R.id.viewVacationExcursionTitle)).getText().toString();
            eId = database.excursionDao().addExcursion(new Excursion(eId, title, new Date().getTime(), id));
        }
        else{
            title = database.excursionDao().getExcursion(eId).Title();
        }
        //this variable must be final for use in the below classes
        final long excursionId = eId;

        View excursion = MainActivity.LI.inflate(R.layout.excursion_entry, null);
        ((TextView)excursion.findViewById(R.id.excursionEntryTitle)).setText(title);
        ((Button)excursion.findViewById(R.id.excursionEntryReplace)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTitle = ((EditText)findViewById(R.id.viewVacationExcursionTitle)).getText().toString();
                Excursion newExcursion = new Excursion(excursionId, newTitle, new Date().getTime(), id);
                database.excursionDao().updateExcursion(newExcursion);

                ((TextView)excursion.findViewById(R.id.excursionEntryTitle)).setText(newTitle);
            }
        });
        ((Button)excursion.findViewById(R.id.excursionEntryDelete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Excursion delete");
                database.excursionDao().deleteExcursion(database.excursionDao().getExcursion(excursionId));
                ((ViewGroup)findViewById(R.id.viewVacationExcursions)).removeView(excursion);
            }
        });

        ((ViewGroup)findViewById(R.id.viewVacationExcursions)).addView(excursion);
    }
}
