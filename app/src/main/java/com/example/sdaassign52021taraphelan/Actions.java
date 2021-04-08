package com.example.sdaassign52021taraphelan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.text.format.DateFormat;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
    private String documentId;
    private String pastAction;
    public int selectedSpinnerPosition;
    public int newCollectionSizeInt;
    public TextView suggestionLine2;
    public TextView suggestionLine4;
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
        Button inspireMe = root.findViewById(R.id.inspireMe);
        Button suggestionButton = root.findViewById(R.id.suggestionButton);
        suggestionLayout = root.findViewById(R.id.suggestionLayout);
        suggestionLine2 = root.findViewById(R.id.suggestionLine2);
        suggestionLine4 = root.findViewById(R.id.suggestionLine4);

        setUpSpinner();

        Log.i(TAG, "setUpSuggestion() being called from onCreateView()");
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
                                        Log.i(TAG, "if clause and numberOfDocuments is " + (String.valueOf(doc.get("numberOfDocuments"))));
                                        long newCollectionSize = (Long.parseLong(String.valueOf(doc.get("numberOfDocuments")))) + 1;
                                        newCollectionSizeInt = (int) newCollectionSize;

                                        //((long) doc.get("numberOfDocuments")) + 1;


                                        FirebaseFirestore.getInstance().collection(actionedLifeArea)
                                                .document("collectionSize").update("numberOfDocuments", newCollectionSize);
                                        //Log.d ("Document", String.valueOf(doc.getData()));
                                    } else {
                                        Log.i(TAG, "else clause. " + doc.get("numberOfDocuments"));

                                        newCollectionSizeInt = 1;
                                        Map<String, Object> collectionSizeData = new HashMap<>();
                                        collectionSizeData.put(getString(R.string.number_of_documents), newCollectionSizeInt);

                                        db.collection(actionedLifeArea).document("collectionSize")
                                                .set(collectionSizeData);


                                    }

                                    // Add a new document with a generated id.
                                    final Map<String, Object> data = new HashMap<>();
                                    data.put(getString(R.string.action), actionSummary.getText().toString());
                                    Log.i(TAG, "just before putting the data, newCollectionSizeInt is " + newCollectionSizeInt);
                                    data.put("incrementalId", newCollectionSizeInt);

                                    db.collection(actionedLifeArea)
                                            .add(data)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.i(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                                    Log.i(TAG, "selectedSpinnerPosition is " + selectedSpinnerPosition);
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
                                                    Log.i("Chart", "editor.apply(called");
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
                                                    Log.i(TAG, "setUpSuggestion() being called from onClick() after toast message");
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

        inspireMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Inspiration.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onResume() {

        super.onResume();
        setUpSpinner();
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

        Log.i(TAG, "in setUpSuggestion()");

        //finding the life area with the fewest associated actions
        final int minValueIndex = getMin(countersFromSharedPreferences);
        Log.i(TAG, "minValueIndex is " + minValueIndex);
        //hiding the suggestions display if there are no past actions to show
        if (countersFromSharedPreferences[minValueIndex] == 0) {
            Log.i(TAG, "if clause in setUpSuggestion(). Because countersFromSharedPreferences[minValueIndex] is 0, suggestions aren't being shown. Its value is " + countersFromSharedPreferences[minValueIndex] + " and the associated life area is " + lifeAreasFromSharedPreferences[minValueIndex]);
            suggestionLayout.setVisibility(GONE);
        } else {
            Log.i(TAG, "else clause in setUpSuggestion(), indicating the value of this counter is not zero. It is " + countersFromSharedPreferences[minValueIndex]);
            //suggestionLayout.setVisibility(VISIBLE);
            Log.i(TAG, "Else clause. Suggestion should be visible with the life area " + lifeAreasFromSharedPreferences[minValueIndex]);
            suggestionLine2.setText(lifeAreasFromSharedPreferences[minValueIndex]);

            //getting a random number to choose a past action
            Random random = new Random();
            //1 is added to the counter because the radonm number will be between 0 (inclusive) and the argument (exclusive)
            int randomNumber = random.nextInt(countersFromSharedPreferences[minValueIndex] + 1);
            while (randomNumber == 0) {
                randomNumber = random.nextInt(countersFromSharedPreferences[minValueIndex] + 1);
            }
            Log.i(TAG, "randomNumber is " + randomNumber + " and the counter for " + lifeAreasFromSharedPreferences[minValueIndex] + " is " + countersFromSharedPreferences[minValueIndex]);

            //retrieving data from Cloud Firestore. Amended from https://www.youtube.com/watch?v=DP1Bc8XmYWs
            FirebaseFirestore.getInstance()
                    .collection(lifeAreasFromSharedPreferences[minValueIndex])
                    .whereEqualTo("incrementalId", randomNumber)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Log.i(TAG, "in onSuccess");
                            Log.i(TAG, "OnSuccess in setUpSuggestion()");
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : snapshotList) {
                                documentId = snapshot.getId();
                                pastAction = (String) snapshot.get("action");
                                Log.i(TAG, "minValueIndex is " + minValueIndex + " and life area is" + lifeAreasFromSharedPreferences[minValueIndex] + " and past action is " + pastAction);
                            }
                            if (pastAction == null) {
                                Log.i(TAG, "past action == null. It is " + pastAction);
                                suggestionLayout.setVisibility(GONE);
                            } else {
                                Log.i(TAG, "past action is not null. It is " + pastAction);
                                suggestionLine4.setText(pastAction);
                                //resetting pastAction to allow it to be reused
                                pastAction = null;
                                suggestionLayout.setVisibility(VISIBLE);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "OnFailure in setUpSuggestion()");
                            Log.e(TAG, "On Failure: ", e);
                        }
                    });

                                /*
                                //checking if the current borrower is associated with this book already
                                String databaseBorrowerId;
                                databaseBorrowerId = snapshot.getString("currentBorrowerId");
                                try {
                                    if (databaseBorrowerId.equals(borrowerIdFromSharedPrefs)) {
                                        Date loanStartDate = snapshot.getTimestamp("collectionDate").toDate();
                                        String loanStartDateFormatted = DateFormat.format("dd/MM/yyyy", loanStartDate).toString();
                                        Date loanEndDate = snapshot.getTimestamp("nextAvailableDate").toDate();
                                        String loanEndDateFormatted = DateFormat.format("dd/MM/yyyy", loanEndDate).toString();
                                        displayBook.setText(String.format(getString(R.string.already_loaned), usernameFromSharedPrefs, chosenBook, loanStartDateFormatted, loanEndDateFormatted));
                                        displayBookAvailability.setVisibility(GONE);
                                        selectDate.setText(R.string.borrow_again);
                                    }
                                } catch (Exception e) {
                                    Log.d(TAG, "currentBorrowerId field does not exist in the database", e);
                                }

                                //getting Date objects for the next available date and today's date
                                nextAvailableDate = timestamp.toDate();
                                today = todayCalendar.getTime();
                                String formattedNextAvailableDate = DateFormat.format("dd/MM/yyyy", nextAvailableDate).toString();

                                //checking if the book is currently available
                                long difference = getDifferenceBetweenDates(today, nextAvailableDate);

                                //handling book that is currently available
                                if (difference <= 1) {
                                    Log.d(TAG, "Book is available message: " + bookIsAvailableMessage);
                                    displayBookAvailability.setText(bookIsAvailableMessage);
                                    selectDate.setVisibility(View.VISIBLE);

                                    //handling book that will be available within 2 weeks
                                } else if (difference < 14) {
                                    displayBookAvailability.setText(String.format(getString(R.string.book_is_unavailable), formattedNextAvailableDate));
                                    selectDate.setVisibility(View.VISIBLE);

                                    //handling book that won't be available within 2 weeks
                                } else {
                                    Date reservationDate = new Date(nextAvailableDate.getTime() - (14 * 24 * 3600 * 1000));
                                    String formattedReservationDate = DateFormat.format("dd/MM/yyyy", reservationDate).toString();
                                    String bookIsUnavailableToReserveMessage = String.format(getString(R.string.book_is_unavailable_and_cannot_be_reserved), formattedNextAvailableDate, formattedReservationDate);
                                    displayBookAvailability.setText(bookIsUnavailableToReserveMessage);
                                    selectDate.setVisibility(INVISIBLE);
                                    sendOrder.setVisibility(INVISIBLE);
                                }
                            }
                        }

                                 */
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
                minValue = inputArray[i];
                arrayIndex = i;
            }
        }
        return arrayIndex;
    }

}
