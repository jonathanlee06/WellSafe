package com.example.wellsafe.ui.checkin;

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

import com.google.android.gms.vision.barcode.Barcode;

import com.example.wellsafe.R;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CheckInFragment extends Fragment implements View.OnClickListener{

    public static TextView result;
    Button checkIn;
    View view;
    private int CAMERA_PERMISSION_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_checkin, container, false);
        result = (TextView) view.findViewById(R.id.result);
        checkIn = (Button) view.findViewById(R.id.btnScan);
        checkIn.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScan:
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Camera Permission Granted", Toast.LENGTH_SHORT).show();
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
                            .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                                }
                            })
                            .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
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
                //Toast.makeText(getActivity(), "Camera Permission GRANTED", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CheckInCamera.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Camera Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
