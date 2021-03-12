package com.example.sdaassign52021taraphelan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

//TODO: Tidy this and add comments and styling

public class Actions extends Fragment implements AdapterView.OnItemSelectedListener {

    //setting up constants to be used by SharedPreferences
    public static final String SHARED_PREFS = "shared prefs";
    public static final String LIFE_AREA_1 = "life area 1";
    public static final String LIFE_AREA_2 = "life area 2";
    public static final String LIFE_AREA_3 = "life area 3";
    public static final String LIFE_AREA_4 = "life area 4";
    public static final String LIFE_AREA_5 = "life area 5";
    public static final String LIFE_AREA_6 = "life area 6";

    //setting up class-wide variables
    public Spinner spinner;
    public SharedPreferences sharedPreferences;
    public String[] defaultLifeAreas;
    public ArrayList<String> arrayList;
    public String[] lifeAreasFromSharedPreferences;

    public Actions() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //innflating the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_actions, container, false);
/*
        //checking if SharedPreferences values exist
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
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
        */
/*
        //spinner tutorial found at https://www.tutorialspoint.com/how-can-i-add-items-to-a-spinner-in-android
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(sharedPreferences.getString(LIFE_AREA_1,defaultLifeAreas[0]));
        arrayList.add(sharedPreferences.getString(LIFE_AREA_2,defaultLifeAreas[1]));
        arrayList.add(sharedPreferences.getString(LIFE_AREA_3,defaultLifeAreas[2]));
        arrayList.add(sharedPreferences.getString(LIFE_AREA_4,defaultLifeAreas[3]));
        arrayList.add(sharedPreferences.getString(LIFE_AREA_5,defaultLifeAreas[4]));
        arrayList.add(sharedPreferences.getString(LIFE_AREA_6,defaultLifeAreas[5]));


 */
       spinner = root.findViewById(R.id.spinner);
/*
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

 */

        setUpSpinner();

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("health").document("yUFXrBaEkQJJP5eapbhV");

        // method obtained from https://www.youtube.com/watch?v=kfvOqGSCmW4
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    String shriver = String.valueOf(doc.get("action"));
                    if (doc.exists()) {
                        Log.d ("Document", String.valueOf(doc.get("action")));
                        //Log.d ("Document", String.valueOf(doc.getData()));
                    } else {
                        Log.d("Document", "No data");
                    }
                }
            }
        });

        return root;
    }

    @Override
    public void onResume() {

        super.onResume();
        setUpSpinner();
    }

    public void setUpSpinner () {

        //checking if SharedPreferences values exist
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
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

        //spinner tutorial found at https://www.tutorialspoint.com/how-can-i-add-items-to-a-spinner-in-android
        arrayList = new ArrayList<>();

        for (int i = 0; i < lifeAreasFromSharedPreferences.length; i++) {
            arrayList.add(lifeAreasFromSharedPreferences[i]);
        }

        /*arrayList.add(sharedPreferences.getString(LIFE_AREA_1,defaultLifeAreas[0]));
        arrayList.add(sharedPreferences.getString(LIFE_AREA_2,defaultLifeAreas[1]));
        arrayList.add(sharedPreferences.getString(LIFE_AREA_3,defaultLifeAreas[2]));
        arrayList.add(sharedPreferences.getString(LIFE_AREA_4,defaultLifeAreas[3]));
        arrayList.add(sharedPreferences.getString(LIFE_AREA_5,defaultLifeAreas[4]));
        arrayList.add(sharedPreferences.getString(LIFE_AREA_6,defaultLifeAreas[5]));*/

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //required method implementation
    }

}
