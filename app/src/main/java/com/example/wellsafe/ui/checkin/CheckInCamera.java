package com.example.wellsafe.ui.checkin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.wellsafe.HomeActivity;
import com.example.wellsafe.R;
import com.example.wellsafe.SignUpActivity;
import com.example.wellsafe.authentication.Users;
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
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        //String id = currentDate + " " + currentTime;

        /* String to split. */
        String res = result.getText();

        String[] temp;
        String delimiter = "\\r?\\n";

        temp = res.split(delimiter);
        String identifier = temp[0];
        String location = temp[1];

        CheckInFragment.result.setText(currentDate);
        CheckInFragment.identifierResult.setText(currentTime);
        onBackPressed();

        //CheckInData checkIn = new CheckInData(location, currentDate, currentTime);
        CheckInData checkIn = new CheckInData(location, currentDate, currentTime);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        String id = databaseReference.push().getKey();
        databaseReference.child("Check-In History").child(id).setValue(checkIn).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(HomeActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        //onBackPressed();
                    }
                });
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



}