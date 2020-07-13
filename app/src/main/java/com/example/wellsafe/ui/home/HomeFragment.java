package com.example.wellsafe.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wellsafe.R;
import com.example.wellsafe.ui.checkin.CheckInCamera;
import com.kyleduo.switchbutton.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class HomeFragment extends Fragment {

    ArrayList<String> malaysiaList = new ArrayList<>();
    public static String inputData;
    View view;
    public static TextView totalCases;
    public static TextView totalRecoveries;
    public static TextView text_home;
    public static TextView proximityRating;
    JSONObject malaysiaData;
    public static int confirmed;
    public static int recovered;
    
    //Tracing
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    ArrayList<String> deviceNearby = new ArrayList<>();
    boolean devicePresent = false;
    public static boolean checked = false;
    public static SwitchButton distancingSwitch;

    private int LOCATION_PERMISSION_CODE = 1;
    String[] PERMISSIONS = {

    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         /*try {
            get_json();

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        view = inflater.inflate(R.layout.fragment_home, container, false);
        totalCases = (TextView) view.findViewById(R.id.totalCases);
        totalRecoveries = (TextView) view.findViewById(R.id.totalRecoveries);
        text_home = (TextView) view.findViewById(R.id.text_home);
        proximityRating = (TextView) view.findViewById(R.id.proximityRating);
        totalCases.setText(String.valueOf(confirmed));
        totalRecoveries.setText(String.valueOf(recovered));

        distancingSwitch = (SwitchButton) view.findViewById(R.id.distancingStatusSwitch);
        if(checked){
            distancingSwitch.setChecked(true);
            startTracing();
        } else{
            distancingSwitch.setChecked(false);
            //stopTracing();
        }
        distancingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if(isChecked){
                        checked = true;
                        startTracing();

                    } else{
                        checked = false;
                        stopTracing();
                    }

                } else {
                    requestLocationPermission();
                    if(isChecked){
                        checked = true;
                        startTracing();

                    } else{
                        checked = false;
                        stopTracing();
                    }
                }


            }

        });
        
        
        return view;
    }

    public void startTracing(){
        enableBT(view);
        statusCheck();
        proximityRating.setText(R.string.level0);

        // Tracing
        final IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        requireActivity().registerReceiver(mReceiver, filter);
        deviceNearby.clear();
        adapter.startDiscovery();
    }

    public void stopTracing() {
        checked = false;
        proximityRating.setText("Off");
        deviceNearby.clear();
        if (adapter != null) {
            adapter.cancelDiscovery();
        }
        // Unregister broadcast listeners
        requireActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /* try {
            get_json();

        } catch (JSONException e) {
            e.printStackTrace();
        } */
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
        requireActivity().unregisterReceiver(mReceiver);
    }

    public void enableBT(View view){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()){
            Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // The REQUEST_ENABLE_BT constant passed to startActivityForResult() is a locally defined integer (which must be greater than 0), that the system passes back to you in your onActivityResult()
            // implementation as the requestCode parameter.
            int REQUEST_ENABLE_BT = 1;
            startActivityForResult(intentBtEnabled, REQUEST_ENABLE_BT);
        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("GPS Location Permission Needed")
                    .setMessage("GPS Location permission is needed for social distancing status check")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, LOCATION_PERMISSION_CODE);
                            statusCheck();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, LOCATION_PERMISSION_CODE);
        }
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

                int deviceType = device.getType();


                if(deviceType == BluetoothDevice.DEVICE_TYPE_CLASSIC)
                {
                    devicePresent = true;
                    deviceNearby.add(device.getName());
                    /*if (numberOfDevice == 0) {
                        HomeFragment.proximityRating.setText(R.string.level1);
                        HomeFragment.proximityRating.setTextColor(Color.rgb(46,125,50));
                        deviceNearby.clear();
                        adapter.startDiscovery();
                    } else if (numberOfDevice == 2) {
                        HomeFragment.proximityRating.setText(R.string.level2);
                        HomeFragment.proximityRating.setTextColor(Color.rgb(249,168,37));
                        deviceNearby.clear();
                        adapter.startDiscovery();
                    } else if (numberOfDevice == 3) {
                        HomeFragment.proximityRating.setText(R.string.level3);
                        HomeFragment.proximityRating.setTextColor(Color.rgb(230,81,0));
                        deviceNearby.clear();
                        adapter.startDiscovery();
                    } else if (numberOfDevice == 1) {
                        HomeFragment.proximityRating.setText(R.string.level4);
                        HomeFragment.proximityRating.setTextColor(Color.RED);
                        deviceNearby.clear();
                        adapter.startDiscovery();
                    }*/
                }
                else if(deviceType == BluetoothDevice.DEVICE_TYPE_LE)
                {
                    devicePresent = false;
                }
                else if(deviceType == BluetoothDevice.DEVICE_TYPE_DUAL)
                {
                    devicePresent = false;
                }
                else if(deviceType == BluetoothDevice.DEVICE_TYPE_UNKNOWN)
                {
                    devicePresent = false;
                }



                /*for(String s : deviceNearby){
                    Log.d("Device Nearby: ", s);
                }*/
                if(devicePresent){
                    HomeFragment.proximityRating.setText(R.string.level4);
                    HomeFragment.proximityRating.setTextColor(Color.RED);
                    adapter.startDiscovery();
                } else {
                    HomeFragment.proximityRating.setText(R.string.level1);
                    HomeFragment.proximityRating.setTextColor(Color.rgb(46,125,50));
                    adapter.startDiscovery();
                }

                //HomeFragment.proximityRating.setText(R.string.level4);
                //HomeFragment.proximityRating.setTextColor(Color.rgb(230,81,0));




                //HomeFragment.text_home.setText("Found device " + device.getName());
            }
        }
    };

    private void get_json() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                            confirmed = malaysiaData.getInt("confirmed");
                            recovered = malaysiaData.getInt("recovered");

                            //Log.e("location response", malaysiaData.getString("data"));

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
}
