package com.example.sdaassign52021taraphelan;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
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

import static android.view.View.GONE;

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
    public static final String COUNTER_1 = "counter 1";
    public static final String COUNTER_2 = "counter 2";
    public static final String COUNTER_3 = "counter 3";
    public static final String COUNTER_4 = "counter 4";
    public static final String COUNTER_5 = "counter 5";
    public static final String COUNTER_6 = "counter 6";
    public int[] countersFromSharedPreferences;

    //setting up class-wide variables
    public Spinner spinner;
    public SharedPreferences sharedPreferences;
    public String[] defaultLifeAreas;
    public ArrayList<String> arrayList;
    public String[] lifeAreasFromSharedPreferences;
    public static final String TAG = "Actions";
    public String actionedLifeArea;
    public int selectedSpinnerPosition;
    public int newCollectionSizeInt;
    public TextView suggestionLine2;
    public LinearLayout suggestionLayout;

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
        Button suggestionButton = root.findViewById(R.id.suggestionButton);
        suggestionLayout = root.findViewById(R.id.suggestionLayout);
        suggestionLine2 = root.findViewById(R.id.suggestionLine2);

        setUpSpinner();

        setUpSuggestion();

        suggestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggestionLayout.setVisibility(GONE);
            }
        });

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
                                        newCollectionSizeInt = (int) newCollectionSize;

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

                                Log.i(TAG, "selectedSpinnerPosition is " + selectedSpinnerPosition);
                                Log.i(TAG, "newCollectionSizeInt is " + newCollectionSizeInt);


                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                switch (selectedSpinnerPosition) {
                                    case 0:
                                        editor.putInt(COUNTER_1, newCollectionSizeInt);
                                        break;
                                    case 1:
                                        editor.putInt(COUNTER_2, newCollectionSizeInt);
                                        break;
                                    case 2:
                                        editor.putInt(COUNTER_3, newCollectionSizeInt);
                                        break;
                                    case 3:
                                        editor.putInt(COUNTER_4, newCollectionSizeInt);
                                        break;
                                    case 4:
                                        editor.putInt(COUNTER_5, newCollectionSizeInt);
                                        break;
                                    case 5:
                                        editor.putInt(COUNTER_6, newCollectionSizeInt);
                                        break;
                                }

                    /*editor.putString(lifeAreaValue2, LIFE_AREA_2);
                    editor.putString(lifeAreaValue3, LIFE_AREA_3);
                    editor.putString(lifeAreaValue4, LIFE_AREA_4);
                    editor.putString(lifeAreaValue5, LIFE_AREA_5);
                    editor.putString(lifeAreaValue6, LIFE_AREA_6);*/

                                /*editor.putString(LIFE_AREA_2, lifeAreaValue2);
                                editor.putString(LIFE_AREA_3, lifeAreaValue3);
                                editor.putString(LIFE_AREA_4, lifeAreaValue4);
                                editor.putString(LIFE_AREA_5, lifeAreaValue5);
                                editor.putString(LIFE_AREA_6, lifeAreaValue6);*/

                                editor.apply();
                                Log.i(TAG, "the first counter from sharedpreferences is " + sharedPreferences.getInt(COUNTER_1, 22));

                                Log.i(TAG, "the first counter from sharedpreferences phrased differently is " + String.valueOf(sharedPreferences.getInt(COUNTER_1, 16)));

                                //TODO: update the shared preferences counter here using selectedSpinnerPosition (which follows index numbers)
                                /*SharedPreferences.Editor editor = sharedPreferences.edit();
                                countersFromSharedPreferences[selectedSpinnerPosition];

                                editor.putString(LIFE_AREA_2, lifeAreaValue2);
                                editor.putString(LIFE_AREA_3, lifeAreaValue3);
                                editor.putString(LIFE_AREA_4, lifeAreaValue4);
                                editor.putString(LIFE_AREA_5, lifeAreaValue5);
                                editor.putString(LIFE_AREA_6, lifeAreaValue6);

                                editor.apply(); */

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

                setUpSuggestion();

            }
        });

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
        setUpSuggestion();
    }

    public void setUpSpinner() {

        //setting up default life areas in case SharedPreferences have not been saved
        defaultLifeAreas = new String[]{getString(R.string.default_life_area_1),
                getString(R.string.default_life_area_2),
                getString(R.string.default_life_area_3),
                getString(R.string.default_life_area_4),
                getString(R.string.default_life_area_5),
                getString(R.string.default_life_area_6)
        };

        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

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

    public void setUpSuggestion() {
        countersFromSharedPreferences = new int[]{sharedPreferences.getInt(COUNTER_1, 0),
                sharedPreferences.getInt(COUNTER_2, 0),
                sharedPreferences.getInt(COUNTER_3, 0),
                sharedPreferences.getInt(COUNTER_4, 0),
                sharedPreferences.getInt(COUNTER_5, 0),
                sharedPreferences.getInt(COUNTER_6, 0)
        };

        //finding the life area with the fewest associated actions
        int minValueIndex = getMin(countersFromSharedPreferences);
        //hiding the suggestions display if there are no past actions to show
        if (countersFromSharedPreferences[minValueIndex] == 0) {
            suggestionLayout.setVisibility(GONE);
        } else {
            suggestionLine2.setText(lifeAreasFromSharedPreferences[minValueIndex]);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        actionedLifeArea = adapterView.getItemAtPosition(i).toString();

        selectedSpinnerPosition = i;

        Toast.makeText(adapterView.getContext(), actionedLifeArea, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //required method implementation
    }

    /**
     * Method for getting the index at which the smallest int in an array is located.
     * Adapted from https://beginnersbook.com/2014/07/java-finding-minimum-and-maximum-values-in-an-array/
     *
     * @param inputArray
     * @return int - index at which the smallest int in the array is located
     */
    public static int getMin(int[] inputArray) {
        int minValue = inputArray[0];
        int arrayIndex = 0;
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i] < minValue) {
                Log.i(TAG, "if clause, i is " + i + " and inputArray[i] is " + inputArray[i]);
                minValue = inputArray[i];
                arrayIndex = i;
            }
        }
        return arrayIndex;
    }

}
