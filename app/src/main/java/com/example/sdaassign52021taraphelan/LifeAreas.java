package com.example.sdaassign52021taraphelan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Arrays;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_1;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_2;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_3;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_4;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_5;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_6;

public class LifeAreas extends Fragment {

    //TODO: Tidy this and add comments and styling

    //setting up class-wide variables
    private TextView lifeAreasDisplay;
    private EditText lifeArea1EditText;
    private EditText lifeArea2EditText;
    private EditText lifeArea3EditText;
    private EditText lifeArea4EditText;
    private EditText lifeArea5EditText;
    private EditText lifeArea6EditText;
    private EditText[] lifeAreaEditTexts;
    private Button changeLifeAreas;
    private Button saveLifeAreas;
    private Button restoreDefaults;
    public String[] lifeAreasFromSharedPreferences;
    public String yourLifeAreas;
    private String lifeAreaValue1;
    private String lifeAreaValue2;
    private String lifeAreaValue3;
    private String lifeAreaValue4;
    private String lifeAreaValue5;
    private String lifeAreaValue6;
    public SharedPreferences sharedPreferences;
    String[] defaultLifeAreas;

    //setting up constants to be used by SharedPreferences
    public static final String SHARED_PREFS = "shared prefs";
    public static final String LIFE_AREA_1 = "life area 1";
    public static final String LIFE_AREA_2 = "life area 2";
    public static final String LIFE_AREA_3 = "life area 3";
    public static final String LIFE_AREA_4 = "life area 4";
    public static final String LIFE_AREA_5 = "life area 5";
    public static final String LIFE_AREA_6 = "life area 6";

    public static final String TAG = "LifeAreas";


    public LifeAreas() {
        //required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflating the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_life_areas, container, false);

        //checking if SharedPreferences values exist
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        defaultLifeAreas = new String[] {getString(R.string.default_life_area_1),
                getString(R.string.default_life_area_2),
                getString(R.string.default_life_area_3),
                getString(R.string.default_life_area_4),
                getString(R.string.default_life_area_5),
                getString(R.string.default_life_area_6)
        };

        //setting up UI elements to be used by Java
        //TODO: there is an array now instead of using individual EditTexts for each. Update rest of class accordingly
        lifeAreasDisplay = root.findViewById(R.id.displayLifeAreasTextView);
        /*lifeArea1EditText = root.findViewById(R.id.lifeArea1EditText);
        lifeArea2EditText = root.findViewById(R.id.lifeArea2EditText);
        lifeArea3EditText = root.findViewById(R.id.lifeArea3EditText);
        lifeArea4EditText = root.findViewById(R.id.lifeArea4EditText);
        lifeArea5EditText = root.findViewById(R.id.lifeArea5EditText);
        lifeArea6EditText = root.findViewById(R.id.lifeArea6EditText);*/

        lifeAreaEditTexts = new EditText[] {root.findViewById(R.id.lifeArea1EditText), root.findViewById(R.id.lifeArea2EditText), root.findViewById(R.id.lifeArea3EditText), root.findViewById(R.id.lifeArea4EditText), root.findViewById(R.id.lifeArea5EditText),
                root.findViewById(R.id.lifeArea6EditText)};

        changeLifeAreas = root.findViewById(R.id.button2);
        saveLifeAreas = root.findViewById(R.id.button);
        restoreDefaults = root.findViewById(R.id.button3);

        Log.i(TAG, "Tag is working");

        /*String[] lifeAreasFromSharedPreferences = new String[] {sharedPreferences.getString(LIFE_AREA_1, defaultLifeAreas[0]),
                sharedPreferences.getString(LIFE_AREA_2, defaultLifeAreas[1]),
                sharedPreferences.getString(LIFE_AREA_3, defaultLifeAreas[2]),
                sharedPreferences.getString(LIFE_AREA_4, defaultLifeAreas[3]),
                sharedPreferences.getString(LIFE_AREA_5, defaultLifeAreas[4]),
                sharedPreferences.getString(LIFE_AREA_6, defaultLifeAreas[5])};*/

        /*String lifeArea1 = sharedPreferences.getString(LIFE_AREA_1, defaultLifeAreas[0]);
        String lifeArea2 = sharedPreferences.getString(LIFE_AREA_2, defaultLifeAreas[1]);
        String lifeArea3 = sharedPreferences.getString(LIFE_AREA_3, defaultLifeAreas[2]);
        String lifeArea4 = sharedPreferences.getString(LIFE_AREA_4, defaultLifeAreas[3]);
        String lifeArea5 = sharedPreferences.getString(LIFE_AREA_5, defaultLifeAreas[4]);
        String lifeArea6 = sharedPreferences.getString(LIFE_AREA_6, defaultLifeAreas[5]);*/

        //setting the text for each of the EditTexts
        /*lifeArea1EditText.setText(lifeAreasFromSharedPreferences[0]);
        lifeArea2EditText.setText(lifeAreasFromSharedPreferences[1]);
        lifeArea3EditText.setText(lifeAreasFromSharedPreferences[2]);
        lifeArea4EditText.setText(lifeAreasFromSharedPreferences[3]);
        lifeArea5EditText.setText(lifeAreasFromSharedPreferences[4]);
        lifeArea6EditText.setText(lifeAreasFromSharedPreferences[5]);*/

        hideEditTexts();
        saveLifeAreas.setVisibility(GONE);
        restoreDefaults.setVisibility(GONE);

        //adding text to the LifeAreasDisplay TextView
        yourLifeAreas = getString(R.string.your_life_areas);
        setLifeAreasDisplay();

        restoreDefaults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: okay to use same counter variable multiple times?
                //TODO: have I done this already?
                for (int i = 0; i < lifeAreaEditTexts.length; i++) {
                    lifeAreaEditTexts[i].setText(defaultLifeAreas[i]);
                }
                /*lifeArea1EditText.setText(defaultLifeAreas[0]);
                lifeArea2EditText.setText(defaultLifeAreas[1]);
                lifeArea3EditText.setText(defaultLifeAreas[2]);
                lifeArea4EditText.setText(defaultLifeAreas[3]);
                lifeArea5EditText.setText(defaultLifeAreas[4]);
                lifeArea6EditText.setText(defaultLifeAreas[5]); */
            }
        });

        changeLifeAreas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lifeAreasDisplay.setText(yourLifeAreas);

                for (int i = 0; i < lifeAreaEditTexts.length; i++) {
                    lifeAreaEditTexts[i].setText(lifeAreasFromSharedPreferences[i]);
                    lifeAreaEditTexts[i].setVisibility(VISIBLE);
                }

                /*lifeArea1EditText.setVisibility(VISIBLE);
                lifeArea2EditText.setVisibility(VISIBLE);
                lifeArea3EditText.setVisibility(VISIBLE);
                lifeArea4EditText.setVisibility(VISIBLE);
                lifeArea5EditText.setVisibility(VISIBLE);
                lifeArea6EditText.setVisibility(VISIBLE);*/
                changeLifeAreas.setVisibility(GONE);
                restoreDefaults.setVisibility(VISIBLE);
                saveLifeAreas.setVisibility(VISIBLE);
            }
        });

        saveLifeAreas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // getting String values entered by the user
                /*for (int i = 0; i < lifeAreaEditTexts.length; i++) {
                    lifeAreaEditTexts[i].setVisibility(VISIBLE);
                }*/
                lifeAreaValue1 = lifeAreaEditTexts[0].getText().toString();
                lifeAreaValue2 = lifeAreaEditTexts[1].getText().toString();
                lifeAreaValue3 = lifeAreaEditTexts[2].getText().toString();
                lifeAreaValue4 = lifeAreaEditTexts[3].getText().toString();
                lifeAreaValue5 = lifeAreaEditTexts[4].getText().toString();
                lifeAreaValue6 = lifeAreaEditTexts[5].getText().toString();



                if (lifeAreaValue1.isEmpty() || lifeAreaValue2.isEmpty() || lifeAreaValue3.isEmpty() || lifeAreaValue4.isEmpty() || lifeAreaValue5.isEmpty() || lifeAreaValue6.isEmpty()) {
                    Log.i(TAG, "first EditText is empty");
                    Toast.makeText(getContext(), "Please do not leave any field blank", Toast.LENGTH_SHORT).show();
                }

                else {

                    //AlertDialog code found at https://stackoverflow.com/questions/36747369/how-to-show-a-pop-up-in-android-studio-to-confirm-an-order
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setMessage(getString(R.string.warning_message));

                    // Runs if user selects Confirm button
                    builder.setPositiveButton(getString(R.string.confirm),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //saving user inputs to SharedPreferences
                                    SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(LIFE_AREA_1, lifeAreaValue1);
                                    editor.putString(LIFE_AREA_2, lifeAreaValue2);
                                    editor.putString(LIFE_AREA_3, lifeAreaValue3);
                                    editor.putString(LIFE_AREA_4, lifeAreaValue4);
                                    editor.putString(LIFE_AREA_5, lifeAreaValue5);
                                    editor.putString(LIFE_AREA_6, lifeAreaValue6);

                                    // Resets local counters to 0
                                    editor.putInt(COUNTER_1, 0);
                                    editor.putInt(COUNTER_2, 0);
                                    editor.putInt(COUNTER_3, 0);
                                    editor.putInt(COUNTER_4, 0);
                                    editor.putInt(COUNTER_5, 0);
                                    editor.putInt(COUNTER_6, 0);
                                    editor.apply();

                                    Log.i(TAG, "LIFE_AREA_5: " + LIFE_AREA_5);

                                    // Sets up view to display new life areas
                                    hideEditTexts();
                                    saveLifeAreas.setVisibility(GONE);
                                    restoreDefaults.setVisibility(GONE);
                                    changeLifeAreas.setVisibility(VISIBLE);
                                    setLifeAreasDisplay();
                                }
                            });

                    // Runs if user selects Cancel button
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Sets up view to display existing life areas
                            hideEditTexts();
                            saveLifeAreas.setVisibility(GONE);
                            restoreDefaults.setVisibility(GONE);
                            changeLifeAreas.setVisibility(VISIBLE);
                            setLifeAreasDisplay();

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });

        return root;
    }

    public void setLifeAreasDisplay() {
        //TODO: this is copied from Actions.java and used again in Chart.java. Maybe make an abstract class and implement interfaces
        lifeAreasFromSharedPreferences = new String[] {sharedPreferences.getString(LIFE_AREA_1, defaultLifeAreas[0]),
                sharedPreferences.getString(LIFE_AREA_2, defaultLifeAreas[1]),
                sharedPreferences.getString(LIFE_AREA_3, defaultLifeAreas[2]),
                sharedPreferences.getString(LIFE_AREA_4, defaultLifeAreas[3]),
                sharedPreferences.getString(LIFE_AREA_5, defaultLifeAreas[4]),
                sharedPreferences.getString(LIFE_AREA_6, defaultLifeAreas[5])};

        String newLine = getString(R.string.new_line);

        lifeAreasDisplay.setText(yourLifeAreas);
        for (int newLineCounter = 0; newLineCounter < 2; newLineCounter ++) {
            lifeAreasDisplay.append(newLine);
        }

        for (int i = 0; i < lifeAreasFromSharedPreferences.length; i++) {
            lifeAreasDisplay.append(lifeAreasFromSharedPreferences[i] + getString(R.string.new_line));
        }
    }

    public void hideEditTexts() {

        for (int i = 0; i < lifeAreaEditTexts.length; i++) {
            lifeAreaEditTexts[i].setVisibility(GONE);
        }

        /*lifeArea1EditText.setVisibility(GONE);
        lifeArea2EditText.setVisibility(GONE);
        lifeArea3EditText.setVisibility(GONE);
        lifeArea4EditText.setVisibility(GONE);
        lifeArea5EditText.setVisibility(GONE);
        lifeArea6EditText.setVisibility(GONE);*/
    }
}

