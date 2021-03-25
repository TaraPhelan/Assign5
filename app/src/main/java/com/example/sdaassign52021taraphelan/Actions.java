package com.example.sdaassign52021taraphelan;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public final String TAG = "Actions";
    public String actionedLifeArea;

    public Actions() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //innflating the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_actions, container, false);

        final EditText actionSummary = root.findViewById(R.id.actionSummary);
        spinner = root.findViewById(R.id.spinner);
        // TODO: change ids in all layouts
        Button save = root.findViewById(R.id.saveAction);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //adding data to Cloud Firestore
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                // Add a new document with a generated id.
                Map<String, Object> data = new HashMap<>();
                data.put(getString(R.string.action), actionSummary.getText().toString());

                //getting the current collection size
                db.collection(actionedLifeArea)
                        .document("collectionSize")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if (task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult();
                                    if (!(doc.get("numberOfDocuments") == null)) {
                                        Log.i(TAG, String.valueOf(doc.get("numberOfDocuments")));
                                        long newCollectionSize = (Long.parseLong(String.valueOf(doc.get("numberOfDocuments")))) + 1;

                                                    //((long) doc.get("numberOfDocuments")) + 1;



                                        FirebaseFirestore.getInstance().collection(actionedLifeArea)
                                                .document("collectionSize").update("numberOfDocuments", newCollectionSize);
                                        //Log.d ("Document", String.valueOf(doc.getData()));
                                    } else {
                                    Log.i(TAG, "else clause. " + String.valueOf(doc.get("numberOfDocuments")));

                                        Map<String, Object> collectionSizeData = new HashMap<>();
                                        collectionSizeData.put(getString(R.string.number_of_documents), "1");

                                        db.collection(actionedLifeArea).document("collectionSize")
                                                .set(collectionSizeData);

                                    }

                                    /*FirebaseFirestore.getInstance().collection("health")
                                            .document("collectionSize").update("numberOfDocuments", 7);*/
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "On Failure: ", e);
                            }
                        });

                db.collection(actionedLifeArea)
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.i(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                                //clearing the EditText and clearing its focus before showing the success message
                                actionSummary.getText().clear();
                                actionSummary.clearFocus();
                                Toast.makeText(getContext(), getString(R.string.success_message), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i(TAG, "Error adding document", e);
                            }
                        });

            }
        });

        setUpSpinner();

        /*FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("cities").document("LA")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });*/

        return root;
    }

    @Override
    public void onResume() {

        super.onResume();
        setUpSpinner();
    }

    public void setUpSpinner() {

        //checking if SharedPreferences values exist
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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_textview, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        actionedLifeArea = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), actionedLifeArea, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //required method implementation
    }

}
