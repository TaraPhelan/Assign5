package com.example.sdaassign52021taraphelan;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class Chart extends Fragment {


    public Chart() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chart, container, false);

        //radar chart tutorial found at https://www.youtube.com/watch?v=Ii4FbRDvmqI
        RadarChart radarChart = root.findViewById(R.id.radarChart);

        RadarDataSet dataSet = new RadarDataSet(dataValues(), "Life Areas");
        dataSet.setColor(Color.RED);
        /*dataSet.setDrawHighlightCircleEnabled(true);
        dataSet.setHighlightCircleFillColor(Color.BLUE);*/

        RadarData data = new RadarData();
        data.addDataSet(dataSet);
        String[] labels = {"health", "finances", "work", "family", "friends", "learning"};
        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        radarChart.setData(data);
        radarChart.getLegend().setEnabled(false);
        radarChart.getDescription().setEnabled(false);
        //radarChart.setWebAlpha(0);
        //radarChart.setSkipWebLineCount(5);
        //radarChart.setWebColorInner(Color.rgb(0, 255, 0));

        return root;
    }

    private ArrayList<RadarEntry> dataValues() {
        ArrayList<RadarEntry> dataVals = new ArrayList<RadarEntry>();
        dataVals.add(new RadarEntry(4));
        dataVals.add(new RadarEntry(4));
        dataVals.add(new RadarEntry(4));
        dataVals.add(new RadarEntry(4));
        dataVals.add(new RadarEntry(4));
        dataVals.add(new RadarEntry(4));
        return dataVals;
    }
}
