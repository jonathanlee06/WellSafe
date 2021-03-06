package com.jonathanlee.wellsafe.ui.home;

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
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jonathanlee.wellsafe.HomeActivity;
import com.jonathanlee.wellsafe.R;
import com.kyleduo.switchbutton.SwitchButton;

import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    ArrayList<String> malaysiaList = new ArrayList<>();
    public static String inputData;
    View view;
    public static TextView totalCases;
    public static TextView totalRecoveries;
    public static TextView text_home;
    public static TextView proximityRating;
    public static  TextView proximityRatingOff;
    TextView distancingTitle;
    JSONObject malaysiaData;
    public static int confirmed;
    public static int recovered;
    
    //Tracing
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    ArrayList<String> deviceNearby = new ArrayList<>();
    boolean devicePresent;
    public static boolean checked = false;
    public static SwitchButton distancingSwitch;
    boolean isRegistered;
    boolean devicePresentCheck;

    private int LOCATION_PERMISSION_CODE = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        distancingTitle = (TextView) view.findViewById(R.id.distancingTitle);
        totalCases = (TextView) view.findViewById(R.id.totalCases);
        totalRecoveries = (TextView) view.findViewById(R.id.totalRecoveries);

        // Gradient Text Effect //
        TextPaint paint = totalCases.getPaint();
        TextPaint paint2 = totalRecoveries.getPaint();
        TextPaint paint3 = distancingTitle.getPaint();
        float width = paint.measureText("0000");
        float width2 = paint3.measureText("Social Distancing Status");

        Shader textShader = new LinearGradient(0, 0, width, totalCases.getTextSize(),
                new int[]{
                        Color.parseColor("#D31027"),
                        Color.parseColor("#EA384D"),
                }, null, Shader.TileMode.CLAMP);
        Shader textShader2 = new LinearGradient(0, 0, width, totalRecoveries.getTextSize(),
                new int[]{
                        Color.parseColor("#20830A"),
                        Color.parseColor("#26982B"),
                }, null, Shader.TileMode.CLAMP);
        Shader textShader3 = new LinearGradient(0, 0, width2, distancingTitle.getTextSize(),
                new int[]{
                        Color.parseColor("#257CE3"),
                        Color.parseColor("#01BEC4"),
                }, null, Shader.TileMode.CLAMP);
        totalCases.getPaint().setShader(textShader);
        totalRecoveries.getPaint().setShader(textShader2);
        // End Gradient Text Effect //

        proximityRating = (TextView) view.findViewById(R.id.proximityRating);
        proximityRatingOff = (TextView) view.findViewById(R.id.proximityRatingOff);
        totalCases.setText(String.valueOf(confirmed));
        totalRecoveries.setText(String.valueOf(recovered));

        distancingSwitch = (SwitchButton) view.findViewById(R.id.distancingStatusSwitch);
        if(checked){
            distancingSwitch.setChecked(true);
            startTracing();
        } else{
            distancingSwitch.setChecked(false);
            stopTracing();
        }
        distancingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if(isChecked){
                        checked = true;
                        startTracing();
                        proximityRating.setVisibility(View.VISIBLE);
                        proximityRatingOff.setVisibility(View.INVISIBLE);
                    } else{
                        checked = false;
                        stopTracing();
                        proximityRating.setVisibility(View.INVISIBLE);
                        proximityRatingOff.setVisibility(View.VISIBLE);
                    }

                } else {
                    requestLocationPermission();
                    if(isChecked){
                        checked = true;
                        startTracing();
                        proximityRating.setVisibility(View.VISIBLE);
                        proximityRatingOff.setVisibility(View.INVISIBLE);
                    } else{
                        checked = false;
                        stopTracing();
                        proximityRating.setVisibility(View.INVISIBLE);
                        proximityRatingOff.setVisibility(View.VISIBLE);
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
        proximityRating.setTextColor(ContextCompat.getColor(getContext(), R.color.textDefaultColor));

        // Tracing
        final IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        requireActivity().registerReceiver(mReceiver, filter);
        isRegistered = true;
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

        if(isRegistered){
            // Unregister broadcast listeners
            requireActivity().unregisterReceiver(mReceiver);
            isRegistered = false;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (adapter != null) {
            adapter.cancelDiscovery();
        }

        if(isRegistered){
            // Unregister broadcast listeners
            requireActivity().unregisterReceiver(mReceiver);
            isRegistered = false;
        }
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
                HomeFragment.proximityRating.setText(R.string.level1);
                HomeFragment.proximityRating.setTextColor(Color.rgb(46,125,50));
                //deviceNearby.clear();
                adapter.startDiscovery();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                int deviceType = device.getType();

                if(deviceType == BluetoothDevice.DEVICE_TYPE_CLASSIC)
                {
                    boolean deviceList = deviceNearby.isEmpty();

                    if(deviceList){
                        deviceNearby.add(device.getName());
                        Log.e("Bluetooth Device", device.getName());
                        int numDevice = deviceNearby.size();
                        Log.e("Bluetooth Device", device.getName());
                        Log.e("Bluetooth Device Number", String.valueOf(numDevice));
                        if(numDevice >= 1){
                            HomeFragment.proximityRating.setText(R.string.level4);
                            HomeFragment.proximityRating.setTextColor(Color.RED);
                            //((HomeActivity) getActivity()).addNotification();
                            //deviceNearby.clear();
                            adapter.startDiscovery();
                        } else {
                            HomeFragment.proximityRating.setText(R.string.level1);
                            HomeFragment.proximityRating.setTextColor(Color.rgb(46,125,50));
                            deviceNearby.clear();
                            adapter.startDiscovery();

                        }
                    } else {
                        String firstDevice = deviceNearby.get(0);
                        String newDevice = device.getName();

                        if(newDevice.equals(firstDevice)){
                            HomeFragment.proximityRating.setText(R.string.level4);
                            HomeFragment.proximityRating.setTextColor(Color.RED);
                            ((HomeActivity) getActivity()).addNotification();
                            deviceNearby.clear();
                            adapter.startDiscovery();
                        }
                    }
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
            } else {
                HomeFragment.proximityRating.setText(R.string.level1);
                HomeFragment.proximityRating.setTextColor(Color.rgb(46,125,50));
                adapter.startDiscovery();
            }
        }
    };
}
