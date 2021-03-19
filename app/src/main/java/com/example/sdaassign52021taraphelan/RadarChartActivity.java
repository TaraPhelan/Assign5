package com.example.sdaassign52021taraphelan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.protobuf.StringValue;

import java.util.ArrayList;

public class RadarChartActivity extends AppCompatActivity {

    //setting up class-wide variables
    public RadarChart radarChart;
    public final String TAG = "RadarChartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_chart);

        Log.i(TAG, "in OnCreate");

        //radar chart tutorial found at https://www.youtube.com/watch?v=Ii4FbRDvmqI
        radarChart = findViewById(R.id.radarChart);

        Log.i(TAG, "just before data values");
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

    }
    private ArrayList<RadarEntry> dataValues() {
        Log.i(TAG, "in datavalues");

        ArrayList<RadarEntry> dataVals = new ArrayList<RadarEntry>();
        //Log.i(TAG, "collectionSize[0] just before adding to dataVals = " + collectionSize[0]);
        Intent startActivity = getIntent();
        Bundle extras = startActivity.getExtras();
        int[] dataFromIntent = extras.getIntArray("radar chart data");
        Log.i(TAG, String.valueOf(dataFromIntent[0]));
        Log.i(TAG, String.valueOf(dataFromIntent[1]));
        dataVals.add(new RadarEntry(dataFromIntent[0]));
        dataVals.add(new RadarEntry(dataFromIntent[1]));
        dataVals.add(new RadarEntry(dataFromIntent[2]));
        dataVals.add(new RadarEntry(dataFromIntent[3]));
        dataVals.add(new RadarEntry(dataFromIntent[4]));
        dataVals.add(new RadarEntry(dataFromIntent[5]));


        /*dataVals.add(new RadarEntry(4));
        dataVals.add(new RadarEntry(4));
        dataVals.add(new RadarEntry(4));
        dataVals.add(new RadarEntry(4));
        dataVals.add(new RadarEntry(4));
        dataVals.add(new RadarEntry(4));*/
        return dataVals;
    }
}