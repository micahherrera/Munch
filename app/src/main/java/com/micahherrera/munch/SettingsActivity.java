package com.micahherrera.munch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.micahherrera.munch.utils.AutoCompleteAdapter;
import com.micahherrera.munch.utils.SettingsAutoComplete;
import com.micahherrera.munch.foodgrid.FoodGridPresenter;

import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private String term;
    private float latitude;
    private float longitude;
    private int radius_filter;
    private boolean open_now;
    private boolean ll = false;

    private ToggleButton priceOne;
    private ToggleButton priceTwo;
    private ToggleButton priceThree;
    private ToggleButton priceFour;

    private SettingsAutoComplete searchTerm;
    private AutoCompleteTextView location;
    private SeekBar distanceSeekbar;
    private Switch openNowSwitch;
    private TextView distanceDisplay;
    private String locationString;
    private String token;
    private Map autoCompleteMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        distanceDisplay = (TextView) findViewById(R.id.distance_text_display);
        location = (AutoCompleteTextView) findViewById(R.id.location_edit_text);
        location.setThreshold(3);

        distanceSeekbar = (SeekBar) findViewById(R.id.distance_seek_bar);
        openNowSwitch = (Switch) findViewById(R.id.open_now_switch);
        openNowSwitch.setChecked(false);
        searchTerm = (SettingsAutoComplete) findViewById(R.id.settings_search_text);
        priceOne = (ToggleButton) findViewById(R.id.price_button_1);
        priceTwo = (ToggleButton) findViewById(R.id.price_button_2);
        priceThree = (ToggleButton) findViewById(R.id.price_button_3);
        priceFour = (ToggleButton) findViewById(R.id.price_button_4);

        setupSeekbar();
        unpackSettingsBundle();

        searchTerm.setThreshold(3);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

               sendSettingsBundle();

            }
        });
    }

    private void setupSeekbar(){
        distanceSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("TAG", "onProgressChanged: " + progress);
                radius_filter = progress * 400;
                distanceDisplay.setText("within " + Integer.toString((int) (radius_filter / 1609.344))
                        + " mile(s)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void unpackSettingsBundle(){
        Bundle bundle = getIntent().getBundleExtra("bundle");
        term = bundle.getString("term", null);
        if (term != null) {
            searchTerm.setText(term);
        }
        if (bundle.getString("latitude", null) != null) {
            ll = true;
            latitude = Float.parseFloat(bundle.getString("latitude"));
            locationString = bundle.getString("latitude") + ", ";
        }
        if (bundle.getString("longitude", null) != null) {
            longitude = Float.parseFloat(bundle.getString("longitude"));
            locationString += bundle.getString("longitude");
            location.setText(locationString);
        }
        if (bundle.getInt("radius_filter", 4800) != 4800) {
            radius_filter = bundle.getInt("radius_filter");
            distanceDisplay.setText("within " + ((int) (radius_filter / 1069.344)) + " mile(s)");
            distanceSeekbar.setProgress(radius_filter/400);

        } else {
            radius_filter = 4800;
            distanceSeekbar.setProgress(radius_filter/400);
            distanceDisplay.setText("within " + ((int) (radius_filter / 1069.344)) + " mile(s)");
        }
        if (bundle.getString("open_now", "true").equals("false")) {
            open_now = false;
        } else {
            open_now = true;
            openNowSwitch.setChecked(true);

        }

        if (bundle.getInt("$") == 1) {
            priceOne.setChecked(true);
        }
        if (bundle.getInt("$$") == 1) {
            priceTwo.setChecked(true);
        }
        if (bundle.getInt("$$$") == 1) {
            priceThree.setChecked(true);
        }
        if (bundle.getInt("$$$$") == 1) {
            priceFour.setChecked(true);
        }

        token = bundle.getString("token");

        searchTerm.setAdapter(new AutoCompleteAdapter(this, token, Float.toString(latitude),
                Float.toString(longitude)));
    }

    private void sendSettingsBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("term", searchTerm.getText().toString());
        if (ll) {
            bundle.putFloat("latitude", latitude);
            bundle.putFloat("longitude", longitude);
        }
        bundle.putInt("radius_filter", radius_filter);


        if (priceOne.isChecked()) {
            bundle.putInt("$", 1);
        }
        if (priceTwo.isChecked()) {
            bundle.putInt("$$", 1);
        }
        if (priceThree.isChecked()) {
            bundle.putInt("$$$", 1);
        }
        if (priceFour.isChecked()) {
            bundle.putInt("$$$$", 1);
        }
        bundle.putString("open_now", (openNowSwitch.isChecked() ? "true" : "false"));
        Intent returnIntent = new Intent();
        returnIntent.putExtra("bundle", bundle);
        setResult(FoodGridPresenter.SETTINGS_RETURN, returnIntent);
        finish();

    }
}