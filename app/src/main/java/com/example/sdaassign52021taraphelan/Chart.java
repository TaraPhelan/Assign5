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

    public int collectionSize [] = new int[6];
    public final String TAG = "Chart";
    public String[] lifeAreasFromSharedPreferences;
    public SharedPreferences sharedPreferences;
    public String[] defaultLifeAreas;
    public int i;

    //setting up constants to be used by SharedPreferences
    public static final String SHARED_PREFS = "shared prefs";
    public static final String LIFE_AREA_1 = "life area 1";
    public static final String LIFE_AREA_2 = "life area 2";
    public static final String LIFE_AREA_3 = "life area 3";
    public static final String LIFE_AREA_4 = "life area 4";
    public static final String LIFE_AREA_5 = "life area 5";
    public static final String LIFE_AREA_6 = "life area 6";
    public static final String INT = "int";

    public Chart() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chart, container, false);

        Button button = root.findViewById(R.id.button4);
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(INT, 2);

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
                                        editor.putInt(INT, 3);

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



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent startActivity = new Intent(getContext(), RadarChartActivity.class);
                Log.i(TAG, "before going into intent, collectionSize[0] is " + collectionSize[0]);
                Log.i(TAG, "before going into intent, the int in shared preferences is " + sharedPreferences.getInt(INT, 0));
                startActivity.putExtra("radar chart data", collectionSize);
                startActivity.putExtra("int", sharedPreferences.getInt(INT, 17));
                startActivity(startActivity);
            }
        });

        return root;
    }
}
