package com.example.j_paschal_java_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.List;

public class EditExcursionActivity extends AppCompatActivity {
    VacationDatabase database;
    long vacationId = -1;
    long excursionId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_excursion);

        //this screen should never be reached without a vacation id being passed in
        vacationId = getIntent().getLongExtra("vacationId", -1);
        excursionId = getIntent().getLongExtra("excursionId", -1);
        database = VacationDatabase.getDatabase(getApplicationContext());

        Vacation vacation = database.vacationDao().getVacation(vacationId);
        ((TextView)findViewById(R.id.editExcursionVacationTitle)).setText(vacation.Title());

        if(excursionId != -1){
            Excursion e = database.excursionDao().getExcursion(excursionId);
            ((TextView)findViewById(R.id.editExcursionHeader)).setText("Edit excursion for");
            ((EditText)findViewById(R.id.editExcursionTitle)).setText(e.Title());
            ((EditText)findViewById(R.id.editExcursionDate)).setText(AddVacationActivity.DateToShortString(e.Date()));
        }
    }

    public void SaveClick(View view){
        String title = ((EditText)findViewById(R.id.editExcursionTitle)).getText().toString();
        long date = new Date(((EditText)findViewById(R.id.editExcursionDate)).getText().toString()).getTime();

        if(excursionId != -1){
            Excursion excursion = new Excursion(excursionId, title, date, vacationId);
            database.excursionDao().updateExcursion(excursion);
        }
        else {
            Excursion excursion = new Excursion(title, date, vacationId);
            database.excursionDao().addExcursion(excursion);
        }

        Intent intent = new Intent(this, ViewVacationActivity.class);
        intent.putExtra("id", vacationId);
        startActivity(intent);
    }
}
