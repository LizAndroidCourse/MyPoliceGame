package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.Button;


public class MainScreen extends AppCompatActivity {

    final private int SENSOR_MODE = 0 ;
    final private int MANUAL_MODE = 1 ;
    private Button sensor_BTN;
    private Button manual_BTN;
    private Button exit_BTN;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
        sensor_BTN = findViewById(R.id.sensor_button);
        sensor_BTN.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
                openSensorActivity();
            } });
         manual_BTN = findViewById(R.id.manual_button);
         manual_BTN.setOnClickListener(new OnClickListener() {
             public void onClick(View v)
             {
                 openManualActivity();
             } });
        exit_BTN=findViewById(R.id.Exit_BTN);
        exit_BTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void openSensorActivity(){
        Intent intent = new Intent(this, Game.class);
        intent.putExtra("MODE", SENSOR_MODE);
        startActivity(intent);
        this.finish();
    }
    public void openManualActivity(){
        Intent intent = new Intent(this, Game.class);
        intent.putExtra("MODE", MANUAL_MODE);
        startActivity(intent);
        this.finish();
    }
}
