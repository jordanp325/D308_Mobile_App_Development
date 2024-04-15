package com.example.j_paschal_java_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ViewExcursionActivity extends AppCompatActivity {
    VacationDatabase database;
    long vacationId = -1;
    long excursionId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_excursion);
        getSupportActionBar().setTitle("View Excursion");

        //this screen should never be reached without a vacation id being passed in
        vacationId = getIntent().getLongExtra("vacationId", -1);
        excursionId = getIntent().getLongExtra("excursionId", -1);
        database = VacationDatabase.getDatabase(getApplicationContext());

        Vacation vacation = database.vacationDao().getVacation(vacationId);
        Excursion excursion = database.excursionDao().getExcursion(excursionId);
        ((TextView)findViewById(R.id.viewExcursionTitle)).setText(excursion.Title());
        ((TextView)findViewById(R.id.viewExcursionDate)).setText(AddVacationActivity.DateToShortString(excursion.Date()));
        ((TextView)findViewById(R.id.viewExcursionVacationTitle)).setText(vacation.Title());
        ((CheckBox)findViewById(R.id.viewExcursionNotify)).setChecked(excursion.Notify());
    }

    public void NotifyClick(View view){
        Excursion excursion = database.excursionDao().getExcursion(excursionId);
        excursion.SetNotification(((CheckBox)findViewById(R.id.viewExcursionNotify)).isChecked());
        database.excursionDao().updateExcursion(excursion);
    }

    public void BackClick(View view){
        Intent intent = new Intent(this, ViewVacationActivity.class);
        intent.putExtra("id", vacationId);
        startActivity(intent);
    }

    public void ClickEdit(View view){
        Intent intent = new Intent(this, EditExcursionActivity.class);
        intent.putExtra("vacationId", vacationId);
        intent.putExtra("excursionId", excursionId);
        startActivity(intent);
    }

    public void ClickDelete(View view){
        database.excursionDao().deleteExcursion(database.excursionDao().getExcursion(excursionId));
        BackClick(view);
    }
}
