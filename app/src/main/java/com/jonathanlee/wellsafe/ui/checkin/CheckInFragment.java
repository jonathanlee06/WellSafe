package com.jonathanlee.wellsafe.ui.checkin;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jonathanlee.wellsafe.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class CheckInFragment extends Fragment implements View.OnClickListener{

    public static TextView result;
    public static TextView identifierResult;
    public static CardView fragmentHistoryCard;
    Button checkIn;
    Button viewHistory;
    TextView latestCheckInTitle;
    public static TextView locationName;
    public static TextView dateTime;
    public static TextView temp;
    View view;
    private int CAMERA_PERMISSION_CODE = 1;

    public static String locationFragment;
    public static String dateTimeFragment;
    public static String temperatureFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_checkin, container, false);

        checkIn = (Button) view.findViewById(R.id.btnScan);
        viewHistory = (Button) view.findViewById(R.id.btnHistory);
        fragmentHistoryCard = (CardView) view.findViewById(R.id.fragmentHistoryCard);
        latestCheckInTitle = (TextView) view.findViewById(R.id.latestCheckInTitle);
        locationName = (TextView) view.findViewById(R.id.fragmentLocationName);
        dateTime = (TextView) view.findViewById(R.id.fragmentDateTime);
        temp = (TextView) view.findViewById(R.id.fragmentTemp);

        if(locationFragment == null || dateTimeFragment == null || temperatureFragment == null){
            fragmentHistoryCard.setVisibility(View.INVISIBLE);
            latestCheckInTitle.setVisibility(View.INVISIBLE);
        } else {
            fragmentHistoryCard.setVisibility(View.VISIBLE);
            latestCheckInTitle.setVisibility(View.VISIBLE);
            locationName.setText(locationFragment);
            dateTime.setText(dateTimeFragment);
            temp.setText(temperatureFragment);
        }

        checkIn.setOnClickListener(this);
        viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckInHistory.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScan:
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getActivity(), "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), CheckInCamera.class);
                    startActivity(intent);
                } else {
                    requestCameraPermission();
                }
        }
    }

    private void requestCameraPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(getActivity())
                            .setTitle("Camera Permission Needed")
                            .setMessage("Camera permission is needed for check-in QR code scanning")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
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
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Camera Permission GRANTED", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CheckInCamera.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Camera Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
