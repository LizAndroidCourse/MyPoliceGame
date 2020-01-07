package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ScoresMap extends AppCompatActivity {

    MapViewFragment mapViewFragment ;
    public static final String SHARD_PREFS = "SharedPreferences";
    public static final String SCORE_KEY = "scoreKey";
    Button back_BTN;
    ScoresList scoresList;
    SharedPreferences mySharedPreferences;
    ArrayList<Score> scoreArray = new ArrayList<>();
    Adapter_Score adapter_score;
    private RecyclerView score_RV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores_map);
        score_RV = findViewById(R.id.table_score);
         mapViewFragment = new MapViewFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mapFrame, mapViewFragment);
        transaction.commit();
        back_BTN = findViewById(R.id.back_BTN);
        back_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToEndGame();
            }
        });
         createTopTenTable();

    }

    public void backToEndGame() {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        this.finish();
    }

    public void createTopTenTable() {

        mySharedPreferences = getSharedPreferences(SHARD_PREFS, MODE_PRIVATE);
       String strFromJson = mySharedPreferences.getString(SCORE_KEY,null);
        Gson gson = new Gson();
        scoresList = gson.fromJson(strFromJson, ScoresList.class);
        scoreArray = scoresList.getRecords();
       Log.d("score", scoresList.getRecords().get(0).toString());
            setTable();
    }

public void setTable(){
    adapter_score = new Adapter_Score(scoreArray);
    score_RV.setHasFixedSize(true);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    score_RV.setLayoutManager(layoutManager);
    score_RV.setAdapter(adapter_score);
    adapter_score.SetOnItemClickListener(new Adapter_Score.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, Score score) {
            showScoreOnMap(score);
        }
    });
}
    private void showScoreOnMap(Score score) {
        mapViewFragment.placeMarker(score.name,score.latitude,score.longitude);
        Toast.makeText(this, score.toString(), Toast.LENGTH_SHORT).show();
    }}

