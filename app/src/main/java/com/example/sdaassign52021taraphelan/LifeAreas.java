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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_1;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_2;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_3;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_4;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_5;
import static com.example.sdaassign52021taraphelan.Chart.COUNTER_6;

/**
 * LifeAreas class
 *
 * @author Tara Phelan 2021
 * @version 1.0
 */
public class LifeAreas extends Fragment {

    // Sets up class-wide variables
    private TextView lifeAreasDisplay;
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
    public static final String TAG = "LifeAreas";
    public SharedPreferences sharedPreferences;
    String[] defaultLifeAreas;

    // Sets up constants to be used by SharedPreferences
    public static final String SHARED_PREFS = "shared prefs";
    public static final String LIFE_AREA_1 = "life area 1";
    public static final String LIFE_AREA_2 = "life area 2";
    public static final String LIFE_AREA_3 = "life area 3";
    public static final String LIFE_AREA_4 = "life area 4";
    public static final String LIFE_AREA_5 = "life area 5";
    public static final String LIFE_AREA_6 = "life area 6";


    public LifeAreas() {
        // Required empty public constructor
    }

    /**
     * Sets the content to the fragment_life_areas XML layout and inflates the UI
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
        final View root = inflater.inflate(R.layout.fragment_life_areas, container, false);

        // Gets SharedPreferences
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // Sets up default life areas
        defaultLifeAreas = new String[]{getString(R.string.default_life_area_1),
                getString(R.string.default_life_area_2),
                getString(R.string.default_life_area_3),
                getString(R.string.default_life_area_4),
                getString(R.string.default_life_area_5),
                getString(R.string.default_life_area_6)
        };

        // Sets up UI elements to be found by Java
        lifeAreasDisplay = root.findViewById(R.id.displayLifeAreasTextView);
        lifeAreaEditTexts = new EditText[]{root.findViewById(R.id.lifeArea1EditText),
                root.findViewById(R.id.lifeArea2EditText),
                root.findViewById(R.id.lifeArea3EditText),
                root.findViewById(R.id.lifeArea4EditText),
                root.findViewById(R.id.lifeArea5EditText),
                root.findViewById(R.id.lifeArea6EditText)};
        changeLifeAreas = root.findViewById(R.id.changeButton);
        saveLifeAreas = root.findViewById(R.id.button);
        restoreDefaults = root.findViewById(R.id.restoreDefaultsButton);

        // Hides EditTexts and Save and Restore Defaults buttons when the View is first created
        hideEditTexts();
        saveLifeAreas.setVisibility(GONE);
        restoreDefaults.setVisibility(GONE);

        // Adds text to the LifeAreasDisplay TextView
        yourLifeAreas = getString(R.string.your_life_areas);
        setLifeAreasDisplay();

        // Puts default life areas in the EditTexts
        restoreDefaults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < lifeAreaEditTexts.length; i++) {
                    lifeAreaEditTexts[i].setText(defaultLifeAreas[i]);
                }
            }
        });

        // Displays UI elements to allow life areas to be updated
        changeLifeAreas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Updates EditTexts to display current life areas
                lifeAreasDisplay.setText(yourLifeAreas);
                for (int i = 0; i < lifeAreaEditTexts.length; i++) {
                    lifeAreaEditTexts[i].setText(lifeAreasFromSharedPreferences[i]);
                    lifeAreaEditTexts[i].setVisibility(VISIBLE);
                }

                // Hides Change button and displays Save and Restore Defaults buttons
                changeLifeAreas.setVisibility(GONE);
                restoreDefaults.setVisibility(VISIBLE);
                saveLifeAreas.setVisibility(VISIBLE);
            }
        });

        //Updates life areas and resets the local counters to zero
        saveLifeAreas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Gets String values entered by the user
                lifeAreaValue1 = lifeAreaEditTexts[0].getText().toString();
                lifeAreaValue2 = lifeAreaEditTexts[1].getText().toString();
                lifeAreaValue3 = lifeAreaEditTexts[2].getText().toString();
                lifeAreaValue4 = lifeAreaEditTexts[3].getText().toString();
                lifeAreaValue5 = lifeAreaEditTexts[4].getText().toString();
                lifeAreaValue6 = lifeAreaEditTexts[5].getText().toString();

                // Shows an error message if any field is left blank
                if (lifeAreaValue1.isEmpty() ||
                        lifeAreaValue2.isEmpty() ||
                        lifeAreaValue3.isEmpty() ||
                        lifeAreaValue4.isEmpty() ||
                        lifeAreaValue5.isEmpty() ||
                        lifeAreaValue6.isEmpty()) {
                    Log.i(TAG, "one of the EditTexts is empty");
                    Toast.makeText(getContext(), getString(R.string.blank_field_message), Toast.LENGTH_SHORT).show();
                } else {
                    // AlertDialog code found at https://stackoverflow.com/questions/36747369/how-to-show-a-pop-up-in-android-studio-to-confirm-an-order
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setMessage(getString(R.string.warning_message));

                    // Runs if user selects Confirm button
                    builder.setPositiveButton(getString(R.string.confirm),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Saves user inputs to SharedPreferences
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

                                    // Sets up UI elements to hide form and display new life areas
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

                            // Sets up UI elements to hide form and display existing life areas
                            hideEditTexts();
                            saveLifeAreas.setVisibility(GONE);
                            restoreDefaults.setVisibility(GONE);
                            changeLifeAreas.setVisibility(VISIBLE);
                            setLifeAreasDisplay();
                        }
                    });

                    // Creates and shows the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });

        // Returns the View
        return root;
    }

    /**
     * Displays the current life areas in the lifeAreasDisplay EditText
     */
    public void setLifeAreasDisplay() {
        lifeAreasFromSharedPreferences = new String[]{sharedPreferences.getString(LIFE_AREA_1, defaultLifeAreas[0]),
                sharedPreferences.getString(LIFE_AREA_2, defaultLifeAreas[1]),
                sharedPreferences.getString(LIFE_AREA_3, defaultLifeAreas[2]),
                sharedPreferences.getString(LIFE_AREA_4, defaultLifeAreas[3]),
                sharedPreferences.getString(LIFE_AREA_5, defaultLifeAreas[4]),
                sharedPreferences.getString(LIFE_AREA_6, defaultLifeAreas[5])};
        String newLine = getString(R.string.new_line);
        lifeAreasDisplay.setText(yourLifeAreas);

        // Adds new lines under the "Your Life Areas" text
        for (int newLineCounter = 0; newLineCounter < 2; newLineCounter++) {
            lifeAreasDisplay.append(newLine);
        }

        // Adds each life area to the display on a new line
        for (int i = 0; i < lifeAreasFromSharedPreferences.length; i++) {
            lifeAreasDisplay.append(lifeAreasFromSharedPreferences[i] + getString(R.string.new_line));
        }
    }

    /**
     * Hides the EditTexts
     */
    public void hideEditTexts() {
        for (int i = 0; i < lifeAreaEditTexts.length; i++) {
            lifeAreaEditTexts[i].setVisibility(GONE);
        }
    }
}

