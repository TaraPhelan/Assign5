package com.example.sdaassign52021taraphelan;

import android.content.Context;
import android.content.SharedPreferences;
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

/**
 * Chart class
 *
 * @author Tara Phelan 2021
 * @version 1.0
 */
public class Chart extends Fragment {

    // Sets up class-wide variables
    public final String TAG = "Chart";
    public String[] lifeAreasFromSharedPreferences;
    public int[] countersFromSharedPreferences;
    public SharedPreferences sharedPreferences;
    public String[] defaultLifeAreas;
    public RadarChart radarChart;

    // Sets up constants to be used by SharedPreferences
    public static final String SHARED_PREFS = "shared prefs";
    public static final String LIFE_AREA_1 = "life area 1";
    public static final String LIFE_AREA_2 = "life area 2";
    public static final String LIFE_AREA_3 = "life area 3";
    public static final String LIFE_AREA_4 = "life area 4";
    public static final String LIFE_AREA_5 = "life area 5";
    public static final String LIFE_AREA_6 = "life area 6";
    public static final String COUNTER_1 = "counter 1";
    public static final String COUNTER_2 = "counter 2";
    public static final String COUNTER_3 = "counter 3";
    public static final String COUNTER_4 = "counter 4";
    public static final String COUNTER_5 = "counter 5";
    public static final String COUNTER_6 = "counter 6";

    public Chart() {
        // Required empty public constructor
    }

    /**
     * Sets the content to the fragment_chart XML layout and inflates the UI
     *
     * @param inflater           inflates the layout for this fragment
     * @param container          the ViewGroup
     * @param savedInstanceState to hold state information
     * @return returns the View
     * @author Tara Phelan 2021
     * @version 1.0
     * @see androidx.fragment.app.Fragment {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflates the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chart, container, false);

        // Sets up the life areas from SharedPreferences or defaults
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        defaultLifeAreas = new String[]{getString(R.string.default_life_area_1),
                getString(R.string.default_life_area_2),
                getString(R.string.default_life_area_3),
                getString(R.string.default_life_area_4),
                getString(R.string.default_life_area_5),
                getString(R.string.default_life_area_6)
        };
        lifeAreasFromSharedPreferences = new String[]{sharedPreferences.getString(LIFE_AREA_1, defaultLifeAreas[0]),
                sharedPreferences.getString(LIFE_AREA_2, defaultLifeAreas[1]),
                sharedPreferences.getString(LIFE_AREA_3, defaultLifeAreas[2]),
                sharedPreferences.getString(LIFE_AREA_4, defaultLifeAreas[3]),
                sharedPreferences.getString(LIFE_AREA_5, defaultLifeAreas[4]),
                sharedPreferences.getString(LIFE_AREA_6, defaultLifeAreas[5])};

        // Gets the local counters for the radar chart
        getLocalCounters();

        // Radar chart tutorial found at https://www.youtube.com/watch?v=Ii4FbRDvmqI
        radarChart = root.findViewById(R.id.radarChart);
        RadarDataSet dataSet = new RadarDataSet(dataValues(), "Life Areas");
        dataSet.setColor(Color.RED);
        RadarData data = new RadarData();
        data.addDataSet(dataSet);
        String[] labels = {lifeAreasFromSharedPreferences[0],
                lifeAreasFromSharedPreferences[1],
                lifeAreasFromSharedPreferences[2],
                lifeAreasFromSharedPreferences[3],
                lifeAreasFromSharedPreferences[4],
                lifeAreasFromSharedPreferences[5]
        };
        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setTextSize(14);
        radarChart.setData(data);
        radarChart.getLegend().setEnabled(false);
        radarChart.getDescription().setEnabled(false);
        dataSet.setDrawFilled(true);
        dataSet.setDrawValues(false);
        dataSet.setFillColor(Color.RED);

        // Returns the View
        return root;
    }

    /**
     * Calls super.onResume() and getLocalCounters methods
     *
     * @see androidx.fragment.app.Fragment {@link #onResume()}
     */
    @Override
    public void onResume() {
        super.onResume();
        getLocalCounters();

        // Updates the radar chart
        radarChart.notifyDataSetChanged();
    }

    /**
     * Sets up data values for the radar chart
     *
     * @return data values
     */
    private ArrayList<RadarEntry> dataValues() {
        ArrayList<RadarEntry> dataVals = new ArrayList<RadarEntry>();
        dataVals.add(new RadarEntry(countersFromSharedPreferences[0]));
        dataVals.add(new RadarEntry(countersFromSharedPreferences[1]));
        dataVals.add(new RadarEntry(countersFromSharedPreferences[2]));
        dataVals.add(new RadarEntry(countersFromSharedPreferences[3]));
        dataVals.add(new RadarEntry(countersFromSharedPreferences[4]));
        dataVals.add(new RadarEntry(countersFromSharedPreferences[5]));
        return dataVals;
    }

    /**
     * Gets local counters from SharedPreferences and adds them to an int Array
     */
    public void getLocalCounters() {
        countersFromSharedPreferences = new int[]{sharedPreferences.getInt(COUNTER_1, 1),
                sharedPreferences.getInt(COUNTER_2, 0),
                sharedPreferences.getInt(COUNTER_3, 0),
                sharedPreferences.getInt(COUNTER_4, 0),
                sharedPreferences.getInt(COUNTER_5, 0),
                sharedPreferences.getInt(COUNTER_6, 0)};
    }

}
