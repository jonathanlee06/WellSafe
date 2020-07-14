package com.example.wellsafe.ui.checkin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wellsafe.R;

public class CheckInSummary extends AppCompatActivity {

    TextView location;
    TextView name;
    TextView phone;
    TextView dateTime;
    TextView temperature;
    Button summaryClose;
    public static String locationSummary;
    public static String nameSummary;
    public static String phoneSummary;
    public static String dateTimeSummary;
    public static String temperatureSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_summary);

        location = (TextView) findViewById(R.id.summaryLocation);
        /*name = (TextView) findViewById(R.id.summaryFullName);
        phone = (TextView) findViewById(R.id.summaryPhone);*/
        dateTime = (TextView) findViewById(R.id.summaryDateTime);
        temperature = (TextView) findViewById(R.id.summaryTemperature);
        summaryClose = (Button) findViewById(R.id.summaryClose);


        location.setText(locationSummary);
        /*name.setText(nameSummary);
        phone.setText(phoneSummary);*/
        dateTime.setText(dateTimeSummary);
        temperature.setText(temperatureSummary + (char) 0x00B0 + "C");

        summaryClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}