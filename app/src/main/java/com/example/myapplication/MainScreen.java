package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainScreen extends AppCompatActivity {

    private Button start_BTN;
    private Button exit_BTN;

    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_BTN = findViewById(R.id.Start_BTN);
        start_BTN.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
                openNewActivity();
            } });
        exit_BTN=findViewById(R.id.Exit_BTN);
        exit_BTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void openNewActivity(){
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }

}
