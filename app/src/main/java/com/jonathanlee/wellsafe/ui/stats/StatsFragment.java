package com.jonathanlee.wellsafe.ui.stats;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jonathanlee.wellsafe.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class StatsFragment extends Fragment {

    View view;
    public static TextView totalCases;
    public static TextView totalRecovered;
    public static TextView totalDeaths;
    public static TextView totalActive;
    LineChart lineChart;
    public static int confirmed;
    public static int recovered;
    public static int deaths;
    public static int active;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stats, container, false);

        // Find XML elements
        totalCases = (TextView) view.findViewById(R.id.malaysiaTotalCase);
        totalRecovered = (TextView) view.findViewById(R.id.malaysiaTotalRecovered);
        totalDeaths = (TextView) view.findViewById(R.id.malaysiaTotalDeaths);
        totalActive = (TextView) view.findViewById(R.id.malaysiaTotalActive);

        // Set variables
        totalCases.setText(String.valueOf(confirmed));
        totalRecovered.setText(String.valueOf(recovered));
        totalDeaths.setText(String.valueOf(deaths));
        totalActive.setText(String.valueOf(active));


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        List<String> xAxisValues = new ArrayList<>(Arrays.asList("0", "11/7", "12/7", "13/7", "14/7", "15/7", "16/7"));
        List<Entry> incomeEntries = getCases();
        dataSets = new ArrayList<>();
        LineDataSet set1;

        set1 = new LineDataSet(incomeEntries, "Income");
        set1.setColor(Color.rgb(65, 168, 121));
        set1.setValueTextColor(Color.rgb(55, 70, 73));
        set1.setValueTextSize(10f);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSets.add(set1);

        //customization
        lineChart = (LineChart) view.findViewById(R.id.activeLineChart);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setExtraLeftOffset(15);
        lineChart.setExtraRightOffset(15);
        //to hide background lines
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);

        //to hide right Y and top X border
        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(false);
        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setEnabled(true);
        XAxis topXAxis = lineChart.getXAxis();
        topXAxis.setEnabled(false);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        set1.setLineWidth(4f);
        set1.setCircleRadius(3f);
        set1.setDrawValues(false);

        //String setter in x-Axis
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.animateX(500);
        lineChart.invalidate();
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);

        return view;
    }

    public void renderData() {
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setAxisMaximum(10f);
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawLimitLinesBehindData(true);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(350f);
        leftAxis.setAxisMinimum(0f);
        //leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);

        lineChart.getAxisRight().setEnabled(false);
        setData();
    }

    private void setData() {

        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(1, 50));
        values.add(new Entry(2, 100));
        values.add(new Entry(3, 80));
        values.add(new Entry(4, 120));
        values.add(new Entry(5, 110));
        values.add(new Entry(7, 150));
        values.add(new Entry(8, 250));
        values.add(new Entry(9, 190));

        LineDataSet set1;
        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Sample Data");
            set1.setDrawIcons(false);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.DKGRAY);
            set1.setCircleColor(Color.DKGRAY);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_blue);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.DKGRAY);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            lineChart.setData(data);
        }
    }

    private List<Entry> getCases() {
        ArrayList<Entry> incomeEntries = new ArrayList<>();

        incomeEntries.add(new Entry(1, 64));
        incomeEntries.add(new Entry(2, 74));
        incomeEntries.add(new Entry(3, 79));
        incomeEntries.add(new Entry(4, 77));
        incomeEntries.add(new Entry(5, 81));
        incomeEntries.add(new Entry(6, 74));
        return incomeEntries.subList(0, 6);
    }
}
