package com.example.wellsafe;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wellsafe.ui.checkin.CheckInFragment;
import com.example.wellsafe.ui.home.HomeFragment;
import com.example.wellsafe.ui.profile.ProfileFragment;
import com.example.wellsafe.ui.stats.StatsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class HomeActivity extends AppCompatActivity {

    JSONObject malaysiaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            this.getSupportActionBar().hide();
            get_json();
        }
        catch (NullPointerException | JSONException e){}
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);
        //new HomeFragment();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /* AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_stats, R.id.navigation_scan, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController); */
    }

    @Override
    public void onBackPressed() {
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
                            StatsFragment.confirmed = malaysiaData.getInt("confirmed");
                            StatsFragment.recovered = malaysiaData.getInt("recovered");
                            StatsFragment.deaths = malaysiaData.getInt("deaths");
                            StatsFragment.active = malaysiaData.getInt("active");

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

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_stats:
                            selectedFragment = new StatsFragment();
                            break;
                        case R.id.navigation_scan:
                            selectedFragment = new CheckInFragment();
                            break;
                        case R.id.navigation_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, selectedFragment).commit();

                    return true;

                }
            };
}