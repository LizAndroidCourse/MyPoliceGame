package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndGameScreen extends AppCompatActivity {
    private Button main_BTN;
    TextView score_INT;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
       score =  getIntent().getIntExtra("SCORE",score);
        main_BTN = findViewById(R.id.main_BTN);
        main_BTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                moveToMain();
            } });
        score_INT = (TextView)findViewById(R.id.scoreInt_LBL);
        score_INT.setText(" "+score);
    }
    public void  moveToMain(){
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        this.finish();
    }
}
