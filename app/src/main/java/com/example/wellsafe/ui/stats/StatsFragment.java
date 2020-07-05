package com.example.wellsafe.ui.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wellsafe.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StatsFragment extends Fragment {

    View view;
    TextView totalCases;
    public static int confirmed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stats, container, false);
        //totalCases = (TextView) view.findViewById(R.id.totalCases);
        //totalCases.setText(String.valueOf(confirmed));
        return view;
    }
}
