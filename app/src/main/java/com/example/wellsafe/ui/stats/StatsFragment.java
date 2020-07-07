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
    TextView totalRecovered;
    TextView totalDeaths;
    TextView totalActive;
    public static int confirmed;
    public static int recovered;
    public static int deaths;
    public static int active;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stats, container, false);
        totalCases = (TextView) view.findViewById(R.id.malaysiaTotalCase);
        totalRecovered = (TextView) view.findViewById(R.id.malaysiaTotalRecovered);
        totalDeaths = (TextView) view.findViewById(R.id.malaysiaTotalDeaths);
        totalActive = (TextView) view.findViewById(R.id.malaysiaTotalActive);
        totalCases.setText(String.valueOf(confirmed));
        totalRecovered.setText(String.valueOf(recovered));
        totalDeaths.setText(String.valueOf(deaths));
        totalActive.setText(String.valueOf(active));
        return view;
    }
}
