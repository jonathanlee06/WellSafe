package com.example.wellsafe.font;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wellsafe.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FontActivity extends AppCompatActivity {

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = (TextView) findViewById(R.id.homeTitle);

        //Create Font
        Typeface product_sans_regular = Typeface.createFromAsset(getAssets(), getString(R.string.product_sans_regular));
        Typeface product_sans_bold = Typeface.createFromAsset(getAssets(), getString(R.string.product_sans_bold));
        Typeface product_sans_italic = Typeface.createFromAsset(getAssets(), getString(R.string.product_sans_italic));
        Typeface product_sans_bold_italic = Typeface.createFromAsset(getAssets(), getString(R.string.product_sans_bold_italic));

        //Set Font
        title.setTypeface(product_sans_bold);

    }
}
