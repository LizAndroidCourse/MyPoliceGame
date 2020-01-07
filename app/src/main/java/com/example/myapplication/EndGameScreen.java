package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.SharedPreferencesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.*;

import org.json.JSONArray;
import org.json.JSONObject;


public class EndGameScreen extends AppCompatActivity {
    private Button main_BTN;
    private Button score_BTN;
    TextView score_INT;
    int score;
    Score current_score;
    ScoresList scoresList;
    SharedPreferences mySharedPreferences;
    public static final String SHARD_PREFS = "SharedPreferences";
    public static final String SCORE_KEY = "scoreKey";
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    Location current_location;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        score = getIntent().getIntExtra("SCORE", score);
        main_BTN = findViewById(R.id.main_BTN);
        main_BTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                moveToMain();
            }
        });
        score_INT = (TextView) findViewById(R.id.scoreInt_LBL);
        score_INT.setText(" " + score);
        score_BTN = findViewById(R.id.score_BTN);
        score_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToScoreTable();
            }
        });
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkIfScoreTopTen();
    }

    public void checkIfScoreTopTen() {
        mySharedPreferences = getSharedPreferences(SHARD_PREFS, MODE_PRIVATE);
        String strFromJson = mySharedPreferences.getString(SCORE_KEY,null);
        Gson gson = new Gson();
        scoresList = gson.fromJson(strFromJson, ScoresList.class);
        current_score = new Score();
        current_score.score = score;
        boolean isTopten = scoresList.isTopTen(current_score);
        Log.d("isTopTen", isTopten+" ");

        if (isTopten == true) {
            current_location = fetchLocation();
            current_score.setLat(current_location.getLatitude());
            current_score.setLng(current_location.getLongitude());
            current_score.score = score;
            askNameFromUser();
        }
    }

    public void askNameFromUser() {
        final EditText name_edit_text = new EditText(this);
        name_edit_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_DATETIME_VARIATION_NORMAL);
        new AlertDialog.Builder(this)
                .setTitle("Great you in the top ten!")
                .setView(name_edit_text)
                .setMessage("Please enter your name:")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = name_edit_text.getText().toString();
                        current_score.name = str;
                        scoresList.addToTopTen(current_score);
                        scoresList.sortRecords();
                        gson = new Gson();
                        String json = gson.toJson(scoresList);
                        mySharedPreferences = getSharedPreferences(SHARD_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString(SCORE_KEY, json);
                        editor.commit();
                    }
                })
                .setIcon(android.R.drawable.star_on)
                .show();

    }

    public void moveToScoreTable() {
        Intent intent = new Intent(this, ScoresMap.class);
        startActivity(intent);
        this.finish();

    }

    public void moveToMain() {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        this.finish();
    }

    private Location getLatLong() {
        LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            try {
                return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Location fetchLocation() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_ID);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        return getLatLong();
    }
}

