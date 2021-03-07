package com.jonathanlee.wellsafe.ui;

import android.app.Activity;
import android.os.Bundle;

import com.jonathanlee.wellsafe.R;

import androidx.annotation.Nullable;

public class Notification extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
    }
}
