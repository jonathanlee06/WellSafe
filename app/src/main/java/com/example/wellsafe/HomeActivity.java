package com.example.wellsafe;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class HomeActivity extends AppCompatActivity {

    JSONObject malaysiaData;
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    ArrayList<String> deviceNearby = new ArrayList<>();
    boolean devicePresent = false;

    // Tracing
    private BeaconManager beaconManager;

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

        // Tracing

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);

        registerReceiver(mReceiver, filter);
        adapter.startDiscovery();

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
    public void onDestroy() {
        /*unregisterReceiver(mReceiver);
        adapter.cancelDiscovery();*/
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (adapter != null) {
            adapter.cancelDiscovery();
        }
        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
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

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int numberOfDevice = deviceNearby.size();


            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismiss progress dialog
                adapter.startDiscovery();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devicePresent = true;
                int deviceType = device.getType();


                if(deviceType == BluetoothDevice.DEVICE_TYPE_CLASSIC)
                {
                    deviceNearby.add(device.getName());
                }
                else if(deviceType == BluetoothDevice.DEVICE_TYPE_LE)
                {

                }
                else if(deviceType == BluetoothDevice.DEVICE_TYPE_DUAL)
                {

                }
                else if(deviceType == BluetoothDevice.DEVICE_TYPE_UNKNOWN)
                {

                }



                /*for(String s : deviceNearby){
                    Log.d("Device Nearby: ", s);
                }*/


                //HomeFragment.proximityRating.setText(R.string.level4);
                //HomeFragment.proximityRating.setTextColor(Color.rgb(230,81,0));

                if (numberOfDevice <= 1) {
                    HomeFragment.proximityRating.setText(R.string.level1);
                    HomeFragment.proximityRating.setTextColor(Color.rgb(46,125,50));
                } else if (numberOfDevice == 2) {
                    HomeFragment.proximityRating.setText(R.string.level2);
                    HomeFragment.proximityRating.setTextColor(Color.rgb(249,168,37));
                } else if (numberOfDevice == 3) {
                    HomeFragment.proximityRating.setText(R.string.level3);
                    HomeFragment.proximityRating.setTextColor(Color.rgb(230,81,0));
                } else {
                    HomeFragment.proximityRating.setText(R.string.level4);
                    HomeFragment.proximityRating.setTextColor(Color.RED);
                }


                //HomeFragment.text_home.setText("Found device " + device.getName());
            }
        }
    };

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
                            int confirmed = malaysiaData.getInt("confirmed");
                            int recovered = malaysiaData.getInt("recovered");
                            HomeFragment.confirmed = malaysiaData.getInt("confirmed");
                            HomeFragment.recovered = malaysiaData.getInt("recovered");
                            //StatsFragment.confirmed = malaysiaData.getInt("confirmed");
                            //StatsFragment.recovered = malaysiaData.getInt("recovered");
                            //StatsFragment.deaths = malaysiaData.getInt("deaths");
                            //StatsFragment.active = malaysiaData.getInt("active");

                            // Set Text
                            HomeFragment.totalCases.setText(String.valueOf(confirmed));
                            HomeFragment.totalRecoveries.setText(String.valueOf(recovered));

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
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    IntentFilter filter = new IntentFilter();

                    filter.addAction(BluetoothDevice.ACTION_FOUND);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment();
                            registerReceiver(mReceiver, filter);
                            adapter.startDiscovery();
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