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
            getSupportActionBar().setTitle("Edit Excursion");
            Excursion e = database.excursionDao().getExcursion(excursionId);
            ((TextView)findViewById(R.id.editExcursionHeader)).setText("Edit excursion for");
            ((EditText)findViewById(R.id.editExcursionTitle)).setText(e.Title());
            ((EditText)findViewById(R.id.editExcursionDate)).setText(AddVacationActivity.DateToShortString(e.Date()));
        }
        else
            getSupportActionBar().setTitle("Add Excursion");
    }

    public void SaveClick(View view){
        String title = ((EditText)findViewById(R.id.editExcursionTitle)).getText().toString();
        String date = ((EditText)findViewById(R.id.editExcursionDate)).getText().toString();
        Vacation vacation = database.vacationDao().getVacation(vacationId);

        if(title.equals("") || date.equals("")){
            AddVacationActivity.DisplayPopup(this, "Not all fields are filled");
            return;
        }
        if(!AddVacationActivity.StringIsValidDate(date)){
            AddVacationActivity.DisplayPopup(this, "Your date must be valid");
            return;
        }
        if(new Date(date).before(new Date(vacation.StartDate())) || new Date(date).after(new Date(vacation.EndDate()))){
            AddVacationActivity.DisplayPopup(this, "Your excursion date must be during its vacation");
            return;
        }

        if(excursionId != -1){
            Excursion excursion = new Excursion(excursionId, title, new Date(date).getTime(), vacationId);
            database.excursionDao().updateExcursion(excursion);
        }
        else {
            Excursion excursion = new Excursion(title, new Date(date).getTime(), vacationId);
            excursionId = database.excursionDao().addExcursion(excursion);
        }

        Intent intent = new Intent(this, ViewExcursionActivity.class);
        intent.putExtra("vacationId", vacationId);
        intent.putExtra("excursionId", excursionId);
        startActivity(intent);
    }
}
