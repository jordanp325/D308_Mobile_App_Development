package com.example.j_paschal_java_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class AddVacationActivity extends AppCompatActivity {
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

        if(title.equals("") || placeOfStay.equals("") || startDate.equals("") || endDate.equals("")){
            DisplayPopup(this, "Not all fields are filled");
            return;
        }
        else if(!StringIsValidDate(startDate)){
            DisplayPopup(this, "Your start date must be valid");
            return;
        }
        else if(!StringIsValidDate(endDate)){
            DisplayPopup(this, "Your end date must be valid");
            return;
        }
        else if(!(new Date(startDate).before(new Date(endDate)))){
            DisplayPopup(this, "Your start date must be before your end date");
            return;
        }

        if(id == -1) {
            Vacation v = new Vacation(title, placeOfStay, new Date(startDate).getTime(), new Date(endDate).getTime());
            database.vacationDao().addVacation(v);
        }
        else{
            Vacation v = new Vacation(id, title, placeOfStay, new Date(startDate).getTime(), new Date(endDate).getTime());
            v.SetNotification(database.vacationDao().getVacation(id).Notify());
            database.vacationDao().updateVacation(v);
        }

        Intent intent = new Intent(this, ViewVacationActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void ImportCode(View view){
        String code = ((EditText)findViewById(R.id.editVacationImportCode)).getText().toString();
        try {
            Vacation vacation;
            if(id != -1) vacation = MainActivity.ImportVacation(code, id);
            else vacation = MainActivity.ImportVacation(code);
            long id = database.vacationDao().addVacation(vacation);

            Intent intent = new Intent(this, ViewVacationActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
        catch(Exception e){
            DisplayPopup(this, "The import code is missing or incorrectly formatted.");
        }
    }

    public static String DateToShortString(long date){
        return DateToShortString(new Date(date));
    }
    public static String DateToShortString(Date date){
        return (date.getMonth()+1)+"/"+date.getDate()+"/"+(date.getYear()+1900);
    }

    public static boolean StringIsValidDate(String date){
        try{
            Date d = new Date(date);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public static void DisplayPopup(AppCompatActivity activity, String text){
        View popup = MainActivity.LI.inflate(R.layout.popup, null);
        ((TextView)popup.findViewById(R.id.popupText)).setText(text);
        ((Button)popup.findViewById(R.id.popupButton)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((ViewGroup)activity.getWindow().getDecorView().getRootView()).removeView(popup);
            }
        });
        ((ViewGroup)activity.getWindow().getDecorView().getRootView()).addView(popup);

    }
}
