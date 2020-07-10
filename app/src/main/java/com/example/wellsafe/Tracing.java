package com.example.wellsafe;

import android.app.Application;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Tracing extends Application implements BeaconConsumer {

    private static final String TAG = "App";

    public static final String BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    // For identify COVID-19 beacons.
    public static final int MAJOR = 0xC0;
    public static final int MINOR = 0x19;

    public static final int IGNORE_ALERT_PERIOD = 30 * 60 * 1000;
    public static final int IGNORE_SAVE_PERIOD = 15 * 60 * 1000;
    public static final int RESTART_BEACON_INTERVAL = 15 * 60 * 1000;

    public static final float ALERT_DISTANCE = 1.0f;
    public static final float MONITORING_DISTANCE = 2.0f;

    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID = "status";
    public static final String ALERT_CHANNEL_ID = "alert";
    public static final long MILLIS_PER_DAY = 86400 * 1000;
    public static final long SCAN_INTERVAL = 10 * 1000;

    public static final int BEACON_MANUFACTURER = 0x004C;
    public static final int TX_POWER = -65;

    private BeaconManager beaconManager;
    private BackgroundPowerSaver backgroundPowerSaver;
    BeaconParser beaconParser;
    private BeaconTransmitter beaconTransmitter;
    private LocationManager locationManager = null;

    private static Tracing sInstance;

    public static Tracing getInstance(){
        return sInstance;
    }

    Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // iBeacon parser
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BEACON_LAYOUT));
        beaconParser = new BeaconParser().setBeaconLayout(BEACON_LAYOUT);
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        //createNotification();
        backgroundPowerSaver = new BackgroundPowerSaver(this);
        handler = new Handler();
    }

    /*public UUID getRollingProximityIdentifier() {
        long timeStampNow = new Date().getTime();
        long dayNumber = timeStampNow / MILLIS_PER_DAY;
        long todayStart = dayNumber * MILLIS_PER_DAY;
        // Smart phone is pretty fast, It's ok to hash a new one ever 15 minutes.
        //byte[] dailyTracingKey = Tools.genDailyTracingKey(Tools.hexToBytes(Tools.tracingKey), dayNumber);
        // Guess timeIntervalNumber start with 0
        long timeIntervalNumber = (timeStampNow - todayStart) / RESTART_BEACON_INTERVAL;
        return Tools.genRollingProximityIdentifier(dailyTracingKey, timeIntervalNumber);
    }*/


    public void start() {
        startScan();
        //startAdvertise();
    }

    public void stop() {
        stopScan();
        stopAdvertise();
    }

    private void startScan() {
        Log.d(TAG, "Starting scan of beacons");
        beaconManager.bind(this);
    }

    private void stopScan() {
        beaconManager.unbind(this);
    }

    /*public void startAdvertise() {
        Log.d(TAG, "Starting Advertising");
        if (beaconTransmitter == null) {
            beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
        } else {
            beaconTransmitter.stopAdvertising();
        }

        UUID uuid = getRollingProximityIdentifier();
        Log.d(TAG, "Beacon UUID: " + uuid.toString());
        Beacon beacon = new Beacon.Builder()
                .setId1(uuid.toString())
                .setId2(String.valueOf(MAJOR))
                .setId3(String.valueOf(MINOR))
                .setManufacturer(BEACON_MANUFACTURER)
                .setTxPower(TX_POWER)
                .build();

        beaconTransmitter.startAdvertising(beacon);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAdvertise();
            }
        }, RESTART_BEACON_INTERVAL);
    }*/

    private void stopAdvertise() {
        beaconTransmitter.stopAdvertising();
    }

    @Override
    public void onBeaconServiceConnect() {

    }
}
