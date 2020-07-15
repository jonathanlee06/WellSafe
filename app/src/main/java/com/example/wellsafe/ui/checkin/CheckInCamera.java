package com.example.wellsafe.ui.checkin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wellsafe.HomeActivity;
import com.example.wellsafe.R;
import com.example.wellsafe.SignUpActivity;
import com.example.wellsafe.api.FirebaseUtils;
import com.example.wellsafe.authentication.Users;
import com.example.wellsafe.ui.profile.EditProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.Calendar;

public class CheckInCamera extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);


    }

    @Override
    public void handleResult(Result result) {
        // Get Date & Time
        final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        final String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        //String id = currentDate + " " + currentTime;

        /* String to split. */
        String res = result.getText();

        String[] temp;
        String delimiter = "\\r?\\n";

        boolean valid = res.contains(delimiter);

        //Todo: Fix valid checking QR Code
        if(!valid){
            temp = res.split(delimiter);
            String identifier = temp[0];
            Log.e("Identifier", identifier);
            final String location = temp[1];



        /*CheckInFragment.result.setText(currentDate);
        CheckInFragment.identifierResult.setText(currentTime);*/

            if(identifier.equals("WS-CI")){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Temperature Input");
                builder.setMessage("Please type in your temperature");
                builder.setCancelable(false);
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String temperature = input.getText().toString();
                        //CheckInData checkIn = new CheckInData(location, currentDate, currentTime);
                        CheckInData checkIn = new CheckInData(location, currentDate, currentTime, temperature);
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        String id = databaseReference.push().getKey();
                        databaseReference.child("Check-In History").child(id).setValue(checkIn).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //Toast.makeText(HomeActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                setSummary(location, currentDate, currentTime, temperature);
                                onBackPressed();
                                Intent intent = new Intent(CheckInCamera.this, CheckInSummary.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        onBackPressed();
                    }
                });

                builder.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Error!");
                builder.setMessage("Invalid QR Code! Please try again!");
                builder.setCancelable(false);
                builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        onBackPressed();
                    }
                });
                builder.show();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error!");
            builder.setMessage("Invalid QR Code! Please try again!");
            builder.setCancelable(false);
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    //onBackPressed();
                }
            });
            builder.show();
        }







    }

    @Override
    protected void onPause() {
        super.onPause();

        ScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }

    private void setSummary(String location, String date, String time, String temperature){
        String dataTime = date + " " + time;

        CheckInSummary.locationSummary = location;
        CheckInSummary.dateTimeSummary = dataTime;
        CheckInSummary.temperatureSummary = temperature;
        FirebaseUtils fb = new FirebaseUtils();
        fb.getSummaryData();
    }

}