package com.example.wellsafe.ui;

import android.app.Activity;
import android.os.Bundle;

import com.example.wellsafe.R;

import androidx.annotation.Nullable;

public class Notification extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
    }
}
