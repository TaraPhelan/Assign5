package com.example.sdaassign52021taraphelan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Chart extends Fragment {

    //setting up class-wide variables
    public int collectionSize [] = new int[6];
    public final String TAG = "Chart";
    public String[] lifeAreasFromSharedPreferences;
    public int[] countersFromSharedPreferences;
    public SharedPreferences sharedPreferences;
    public String[] defaultLifeAreas;
    public int i;
    public RadarChart radarChart;

    //setting up constants to be used by SharedPreferences
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chart, container, false);

        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putInt(COUNTER_1, 2);

                    /*editor.putString(lifeAreaValue2, LIFE_AREA_2);
                    editor.putString(lifeAreaValue3, LIFE_AREA_3);
                    editor.putString(lifeAreaValue4, LIFE_AREA_4);
                    editor.putString(lifeAreaValue5, LIFE_AREA_5);
                    editor.putString(lifeAreaValue6, LIFE_AREA_6);*/

        editor.apply();

        defaultLifeAreas = new String[] {getString(R.string.default_life_area_1),
                getString(R.string.default_life_area_2),
                getString(R.string.default_life_area_3),
                getString(R.string.default_life_area_4),
                getString(R.string.default_life_area_5),
                getString(R.string.default_life_area_6)
        };

        lifeAreasFromSharedPreferences = new String[] {sharedPreferences.getString(LIFE_AREA_1, defaultLifeAreas[0]),
                sharedPreferences.getString(LIFE_AREA_2, defaultLifeAreas[1]),
                sharedPreferences.getString(LIFE_AREA_3, defaultLifeAreas[2]),
                sharedPreferences.getString(LIFE_AREA_4, defaultLifeAreas[3]),
                sharedPreferences.getString(LIFE_AREA_5, defaultLifeAreas[4]),
                sharedPreferences.getString(LIFE_AREA_6, defaultLifeAreas[5])};

        countersFromSharedPreferences = new int[] {sharedPreferences.getInt(COUNTER_1, 1),
                sharedPreferences.getInt(COUNTER_2, 0),
                sharedPreferences.getInt(COUNTER_3, 0),
                sharedPreferences.getInt(COUNTER_4, 0),
                sharedPreferences.getInt(COUNTER_5, 0),
                sharedPreferences.getInt(COUNTER_6, 0)};

        for (i = 0; i < 5; i++) {
            Log.i(TAG, "i is " + String.valueOf(i));
            //adding data to Cloud Firestore
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            //getting the current collection size

           db.collection(lifeAreasFromSharedPreferences[i])
                        .document("collectionSize")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if (task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult();
                                    if (!(doc.get("numberOfDocuments") == null)) {
                                        collectionSize[i] = Integer.parseInt(String.valueOf(doc.get("numberOfDocuments")));
                                        collectionSize[i] = 4;
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        //editor.putInt(INT, 3);

                    /*editor.putString(lifeAreaValue2, LIFE_AREA_2);
                    editor.putString(lifeAreaValue3, LIFE_AREA_3);
                    editor.putString(lifeAreaValue4, LIFE_AREA_4);
                    editor.putString(lifeAreaValue5, LIFE_AREA_5);
                    editor.putString(lifeAreaValue6, LIFE_AREA_6);*/

                                        editor.apply();
                                    } else {
                                        collectionSize[i] = 0;
                                    }
                                    Log.i(TAG, String.valueOf(collectionSize[i]));
                                    if (i == 4) {
                                        try {
                                            doc.wait();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "On Failure: ", e);
                            }
                        });
            }

        //radar chart tutorial found at https://www.youtube.com/watch?v=Ii4FbRDvmqI
        radarChart = root.findViewById(R.id.radarChart);

        Log.i(TAG, "just before data values");
        RadarDataSet dataSet = new RadarDataSet(dataValues(), "Life Areas");
        dataSet.setColor(Color.RED);
        /*dataSet.setDrawHighlightCircleEnabled(true);
        dataSet.setHighlightCircleFillColor(Color.BLUE);*/

        RadarData data = new RadarData();
        data.addDataSet(dataSet);
        String[] labels = {lifeAreasFromSharedPreferences[0],
                lifeAreasFromSharedPreferences[1],
                lifeAreasFromSharedPreferences[2],
                lifeAreasFromSharedPreferences[3],
                lifeAreasFromSharedPreferences[4],
                lifeAreasFromSharedPreferences[5]
        };

        //String[] labels = {"health", "finances", "work", "family", "friends", "learning"};
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
        Log.i(TAG, "in datavalues");

        ArrayList<RadarEntry> dataVals = new ArrayList<RadarEntry>();

        Log.i(TAG, "counter from sharedprefereences is " + sharedPreferences.getInt(COUNTER_1, 29));
        Log.i(TAG, "countersFromSharedPreferences[0] is " + countersFromSharedPreferences[0]);
        Log.i(TAG, "countersFromSharedPreferences[1] is " + countersFromSharedPreferences[1]);
        Log.i(TAG, "countersFromSharedPreferences[2] is " + countersFromSharedPreferences[2]);
        Log.i(TAG, "countersFromSharedPreferences[3] is " + countersFromSharedPreferences[3]);
        Log.i(TAG, "countersFromSharedPreferences[4] is " + countersFromSharedPreferences[4]);
        Log.i(TAG, "countersFromSharedPreferences[5] is " + countersFromSharedPreferences[5]);
        dataVals.add(new RadarEntry(countersFromSharedPreferences[0]));
        dataVals.add(new RadarEntry(countersFromSharedPreferences[1]));
        dataVals.add(new RadarEntry(countersFromSharedPreferences[2]));
        dataVals.add(new RadarEntry(countersFromSharedPreferences[3]));
        dataVals.add(new RadarEntry(countersFromSharedPreferences[4]));
        dataVals.add(new RadarEntry(countersFromSharedPreferences[5]));
        return dataVals;
    }

}
