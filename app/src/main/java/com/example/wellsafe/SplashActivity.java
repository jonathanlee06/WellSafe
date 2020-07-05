package com.example.wellsafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wellsafe.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth authFirebase;
    private FirebaseUser userFirebase;

    private DatabaseReference database;
    private String userID;
    private ProgressBar spinner;
    JSONObject malaysiaData;

    private boolean backPressToExit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //FetchData process  = new FetchData();
        //process.execute();
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
            get_json();
        }
        catch (NullPointerException | JSONException e){}
        setContentView(R.layout.activity_splash);
        spinner = (ProgressBar) findViewById(R.id.loading);

        //Initialize the firebase
        authFirebase = FirebaseAuth.getInstance();
        userFirebase = authFirebase.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!backPressToExit){
                    if(userFirebase == null){
                        //not logged in, launch the login activity
                        loadSignUpView();
                    } else {
                        loadHomeView();
                    }
                }


            }
        }, 2000  );


    }

    @Override
    public void onBackPressed() {
        backPressToExit = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Are you sure you want to quit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                backPressToExit = false;
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void get_json() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://covid2019-api.herokuapp.com/v2/country/malaysia";

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("API Response", response.toString());
                        try {
                            malaysiaData = response.getJSONObject("data");
                            //totalCases = (TextView) getActivity().findViewById(R.id.totalCases);
                            String country = malaysiaData.getString("location");
                            HomeFragment.confirmed = malaysiaData.getInt("confirmed");
                            HomeFragment.recovered = malaysiaData.getInt("recovered");
                            //Log.e("location response", malaysiaData.getString("data"));
                            //totalCases.setText(String.valueOf(confirmed));
                            //confirmedNum = data.getInt("deaths");
                            //String country = data.getString("location");
                            //Log.e("API Response 2", country);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                    }
                }
        );

        requestQueue.add(objectRequest);

        /*    JSONObject jsonObject = new JSONObject(inputData);
            JSONObject malaysiaData = jsonObject.getJSONObject("data");

            //totalCases.setText(malaysiaData.getString("confirmed"));
        String confirmed = malaysiaData.getString("confirmed");
        Log.d("API test", confirmed + " or not");

            //malaysiaList.add(jsonObject.getString("data"));
            /* malaysiaList.add(jsonObject.getString("deaths"));
            malaysiaList.add(jsonObject.getString("recovered"));
            malaysiaList.add(jsonObject.getString("active")); */
    }

    private void loadLogInView() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void loadSignUpView() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void loadHomeView() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
