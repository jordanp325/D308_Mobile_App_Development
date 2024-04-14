package com.example.j_paschal_java_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class AddVacationActivity  extends AppCompatActivity {
    VacationDatabase database;
    long id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_vacation);

        id = getIntent().getLongExtra("id", -1);
        database = VacationDatabase.getDatabase(getApplicationContext());

        //an id is passed into this screen if it is editing a vacation instead of adding one
        if(id != -1){
            Vacation vacation = database.vacationDao().getVacation(id);
            ((TextView)findViewById(R.id.editVacationHeader)).setText("Edit vacation");

            ((TextView)findViewById(R.id.editVacationTitle)).setText(vacation.Title());
            ((TextView)findViewById(R.id.editVacationPlaceOfStay)).setText(vacation.PlaceOfStay());
            ((TextView)findViewById(R.id.editVacationStartDate)).setText(DateToShortString(new Date(vacation.StartDate())));
            ((TextView)findViewById(R.id.editVacationEndDate)).setText(DateToShortString(new Date(vacation.EndDate())));
        }
    }


    public void CreateVacationOnClick(View view){
        String title = ((EditText)findViewById(R.id.editVacationTitle)).getText().toString();
        String placeOfStay = ((EditText)findViewById(R.id.editVacationPlaceOfStay)).getText().toString();
        String startDate = ((EditText)findViewById(R.id.editVacationStartDate)).getText().toString();
        String endDate = ((EditText)findViewById(R.id.editVacationEndDate)).getText().toString();
        //todo: verify start and end dates
        //todo: verify all fields are filled

        if(id == -1) {
            Vacation v = new Vacation(title, placeOfStay, new Date(startDate).getTime(), new Date(endDate).getTime());
            database.vacationDao().addVacation(v);
        }
        else{
            Vacation v = new Vacation(id, title, placeOfStay, new Date(startDate).getTime(), new Date(endDate).getTime());
            database.vacationDao().updateVacation(v);
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static String DateToShortString(Date date){
        return (date.getMonth()+1)+"/"+date.getDate()+"/"+(date.getYear()+1900);
    }
}
