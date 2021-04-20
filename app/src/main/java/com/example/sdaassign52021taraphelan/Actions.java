package com.example.sdaassign52021taraphelan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.Random;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_1;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_2;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_3;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_4;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_5;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_6;


/**
 * Actions class
 *
 * @author Tara Phelan 2021
 * @version 1.0
 */
public class Actions extends Fragment implements AdapterView.OnItemSelectedListener {

    // Sets up constants to be used by SharedPreferences
    public static final String SHARED_PREFS = "shared prefs";
    public static final String LIFE_AREA_1 = "life area 1";
    public static final String LIFE_AREA_2 = "life area 2";
    public static final String LIFE_AREA_3 = "life area 3";
    public static final String LIFE_AREA_4 = "life area 4";
    public static final String LIFE_AREA_5 = "life area 5";
    public static final String LIFE_AREA_6 = "life area 6";

    // Sets up class-wide variables
    public ArrayList<String> arrayList;
    public int[] countersFromSharedPreferences;
    public int newCollectionSizeInt;
    public int selectedSpinnerPosition;
    public LinearLayout suggestionLayout;
    public SharedPreferences sharedPreferences;
    public Spinner spinner;
    public String[] defaultLifeAreas;
    public String[] lifeAreasFromSharedPreferences;
    public String actionedLifeArea;
    private String pastAction;
    public static final String TAG = "Actions";
    public TextView suggestionLine2;
    public TextView suggestionLine4;

    public Actions() {
        // Required empty public constructor
    }

    /**
     * Sets the content to the fragment_actions XML layout and inflates the UI
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
        View root = inflater.inflate(R.layout.fragment_actions, container, false);

        // Sets up Views to be found by Java
        final EditText actionSummary = root.findViewById(R.id.actionSummary);
        spinner = root.findViewById(R.id.spinner);
        Button saveButton = root.findViewById(R.id.saveAction);
        Button inspireMeButton = root.findViewById(R.id.inspireMe);
        Button suggestionButton = root.findViewById(R.id.suggestionButton);
        suggestionLayout = root.findViewById(R.id.suggestionLayout);
        suggestionLine2 = root.findViewById(R.id.suggestionLine2);
        suggestionLine4 = root.findViewById(R.id.suggestionLine4);

        // Calls method to set up spinner values
        setUpSpinner();

        // Calls method to display a suggestion
        Log.i(TAG, "setUpSuggestion() being called from onCreateView()");
        setUpSuggestion();

        // Hides suggestion if x button is pressed
        suggestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggestionLayout.setVisibility(GONE);
            }
        });

        // Saves action to Cloud Firestore and updates remote and local counters
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Gets the current collection size
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection(actionedLifeArea)
                        .document("collectionSize")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult();

                                    // Increases the numberOfDocuments value by 1 if collection already exists
                                    if (!(doc.get("numberOfDocuments") == null)) {
                                        Log.i(TAG, "numberOfDocuments is " + doc.get("numberOfDocuments"));
                                        long newCollectionSize = (Long.parseLong(String.valueOf(doc.get("numberOfDocuments")))) + 1;
                                        newCollectionSizeInt = (int) newCollectionSize;
                                        FirebaseFirestore.getInstance().collection(actionedLifeArea)
                                                .document("collectionSize").update("numberOfDocuments", newCollectionSize);

                                        // Creates a collectionSize document with numberOfDocuments set to 1 if collection does not already exist
                                    } else {
                                        Log.i(TAG, "A new collection will be created");
                                        newCollectionSizeInt = 1;
                                        Map<String, Object> collectionSizeData = new HashMap<>();
                                        collectionSizeData.put(getString(R.string.number_of_documents), newCollectionSizeInt);
                                        db.collection(actionedLifeArea).document("collectionSize")
                                                .set(collectionSizeData);
                                    }

                                    // Adds a new document to hold the saved action
                                    final Map<String, Object> data = new HashMap<>();
                                    data.put(getString(R.string.action), actionSummary.getText().toString());
                                    Log.i(TAG, "When this action is added, the newCollectionSizeInt is " + newCollectionSizeInt);

                                    // Creates an incrementalId for the new action
                                    data.put("incrementalId", newCollectionSizeInt);
                                    db.collection(actionedLifeArea)
                                            .add(data)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.i(TAG, "DocumentSnapshot was written with ID " + documentReference.getId());
                                                    Log.i(TAG, "selectedSpinnerPosition is " + selectedSpinnerPosition);
                                                    Log.i(TAG, "actionedLifeArea is " + actionedLifeArea);

                                                    // Increases the corresponding local counter by 1
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    int newLocalCounterValue;
                                                    switch (selectedSpinnerPosition) {
                                                        case 0:
                                                            Log.i(TAG, "current local counter value for the first spinner position is " + sharedPreferences.getInt(COUNTER_1, 0));
                                                            newLocalCounterValue = (sharedPreferences.getInt(COUNTER_1, 0)) + 1;
                                                            editor.putInt(COUNTER_1, newLocalCounterValue);
                                                            break;
                                                        case 1:
                                                            newLocalCounterValue = (sharedPreferences.getInt(COUNTER_2, 0)) + 1;
                                                            editor.putInt(COUNTER_2, newLocalCounterValue);
                                                            break;
                                                        case 2:
                                                            newLocalCounterValue = (sharedPreferences.getInt(COUNTER_3, 0)) + 1;
                                                            editor.putInt(COUNTER_3, newLocalCounterValue);
                                                            break;
                                                        case 3:
                                                            newLocalCounterValue = (sharedPreferences.getInt(COUNTER_4, 0)) + 1;
                                                            editor.putInt(COUNTER_4, newLocalCounterValue);
                                                            break;
                                                        case 4:
                                                            newLocalCounterValue = (sharedPreferences.getInt(COUNTER_5, 0)) + 1;
                                                            editor.putInt(COUNTER_5, newLocalCounterValue);
                                                            break;
                                                        case 5:
                                                            newLocalCounterValue = (sharedPreferences.getInt(COUNTER_6, 0)) + 1;
                                                            editor.putInt(COUNTER_6, newLocalCounterValue);
                                                            break;
                                                    }
                                                    editor.apply();

                                                    // Clears the EditText
                                                    actionSummary.getText().clear();

                                                    // Removes focus from the EditText
                                                    actionSummary.clearFocus();

                                                    // Shows the success message
                                                    Toast.makeText(getContext(), getString(R.string.success_message), Toast.LENGTH_SHORT).show();

                                                    // Updates the suggestion
                                                    Log.i(TAG, "setUpSuggestion() being called from onClick() method after toast message");
                                                    setUpSuggestion();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.i(TAG, "Error adding document", e);
                                                }
                                            });
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
        });

        // Sets up OnClickListener to open Inspiration Activity
        inspireMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Sets up Intent and passes the current neglected life area as an extra
                Intent intent = new Intent(getContext(), Inspiration.class);
                intent.putExtra("Neglected Life Area", suggestionLine2.getText());
                startActivity(intent);
            }
        });

        // Returns the view
        return root;
    }

    /**
     * Calls super.onResume() and setUpSpinner methods
     *
     * @see androidx.fragment.app.Fragment {@link #onResume()}
     */
    @Override
    public void onResume() {
        super.onResume();
        setUpSpinner();
    }

    /**
     * Sets up the Spinner
     */
    public void setUpSpinner() {

        // Adds default life areas to a String Array in case SharedPreferences have not been saved
        defaultLifeAreas = new String[]{getString(R.string.default_life_area_1),
                getString(R.string.default_life_area_2),
                getString(R.string.default_life_area_3),
                getString(R.string.default_life_area_4),
                getString(R.string.default_life_area_5),
                getString(R.string.default_life_area_6)
        };

        // Creates an Array to hold life areas from SharedPreferences or defaults
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        lifeAreasFromSharedPreferences = new String[]{sharedPreferences.getString(LIFE_AREA_1, defaultLifeAreas[0]),
                sharedPreferences.getString(LIFE_AREA_2, defaultLifeAreas[1]),
                sharedPreferences.getString(LIFE_AREA_3, defaultLifeAreas[2]),
                sharedPreferences.getString(LIFE_AREA_4, defaultLifeAreas[3]),
                sharedPreferences.getString(LIFE_AREA_5, defaultLifeAreas[4]),
                sharedPreferences.getString(LIFE_AREA_6, defaultLifeAreas[5])};

        // Spinner tutorial found at https://www.tutorialspoint.com/how-can-i-add-items-to-a-spinner-in-android
        arrayList = new ArrayList<>();
        for (int i = 0; i < lifeAreasFromSharedPreferences.length; i++) {
            arrayList.add(lifeAreasFromSharedPreferences[i]);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_textview, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    /**
     * Sets up the suggestion display
     */
    public void setUpSuggestion() {

        // Gets local counters from SharedPreferences
        countersFromSharedPreferences = new int[]{sharedPreferences.getInt(COUNTER_1, 0),
                sharedPreferences.getInt(COUNTER_2, 0),
                sharedPreferences.getInt(COUNTER_3, 0),
                sharedPreferences.getInt(COUNTER_4, 0),
                sharedPreferences.getInt(COUNTER_5, 0),
                sharedPreferences.getInt(COUNTER_6, 0)
        };

        // Finds the life area with the fewest associated actions
        final int minValueIndex = getMin(countersFromSharedPreferences);
        Log.i(TAG, "minValueIndex is " + minValueIndex);

        // Hides the suggestions display if the user has a life area with no associated actions or if the device orientation is landscape
        if (countersFromSharedPreferences[minValueIndex] == 0 || getResources().getConfiguration().orientation == 2) {
            Log.i(TAG, "Suggestions aren't shown. Either the orientation is landscape or the local counter for " + lifeAreasFromSharedPreferences[minValueIndex] + " is 0");
            suggestionLayout.setVisibility(GONE);

            // Shows the suggestion display
        } else {

            // Displays the most neglected life area
            Log.i(TAG, "The value of the local counter for " + lifeAreasFromSharedPreferences[minValueIndex] + " is " + countersFromSharedPreferences[minValueIndex]);
            suggestionLine2.setText(lifeAreasFromSharedPreferences[minValueIndex]);

            // Gets a random number to choose a past action
            Random random = new Random();

            // Adds 1 to the counter because the random number will be between 0 (inclusive) and the argument (exclusive)
            int randomNumber = random.nextInt(countersFromSharedPreferences[minValueIndex] + 1);

            // Chooses a new random number if 0 was chosen
            while (randomNumber == 0) {
                randomNumber = random.nextInt(countersFromSharedPreferences[minValueIndex] + 1);
            }
            Log.i(TAG, "randomNumber is " + randomNumber + " and the counter for " + lifeAreasFromSharedPreferences[minValueIndex] + " is " + countersFromSharedPreferences[minValueIndex]);

            // Retrieves the action from Cloud Firestore which corresponds to the random number. Amended from https://www.youtube.com/watch?v=DP1Bc8XmYWs
            FirebaseFirestore.getInstance()
                    .collection(lifeAreasFromSharedPreferences[minValueIndex])
                    .whereEqualTo("incrementalId", randomNumber)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : snapshotList) {
                                pastAction = (String) snapshot.get("action");
                                Log.i(TAG, "life area is" + lifeAreasFromSharedPreferences[minValueIndex] + " and past action is " + pastAction);
                            }

                            // Hides the suggestion if the past action is null
                            if (pastAction == null) {
                                Log.i(TAG, "past action == null");
                                suggestionLayout.setVisibility(GONE);

                                // Shows the suggestion if the past action is not null
                            } else {
                                Log.i(TAG, "past action is not null. It is " + pastAction);
                                suggestionLine4.setText(pastAction);

                                // Resets the pastAction variable to allow it to be reused
                                pastAction = null;
                                suggestionLayout.setVisibility(VISIBLE);
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
    }

    /**
     * Handles spinner item selection
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        actionedLifeArea = adapterView.getItemAtPosition(i).toString();
        selectedSpinnerPosition = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //required method implementation
    }

    /**
     * Gets the index at which the smallest int in an array is located
     * Adapted from https://beginnersbook.com/2014/07/java-finding-minimum-and-maximum-values-in-an-array/
     *
     * @param inputArray
     * @return int index at which the smallest int in the array is located
     */
    public static int getMin(int[] inputArray) {
        int minValue = inputArray[0];
        int arrayIndex = 0;
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i] < minValue) {
                minValue = inputArray[i];
                arrayIndex = i;
            }
        }
        return arrayIndex;
    }
}
