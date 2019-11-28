package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Game extends AppCompatActivity {

    private Button right_BTN;
    private Button left_BTN;
    private int gamePaths = 3;
    private ImageView[] heartArry;
    int countLife=3;
    private ImageView red_car;
    private ImageView imageView_police;
    int sizeX, sizeY;
    Display display;
    Handler handler;
    Point size;
    int dp_layout_width_police = 100;
    int dp_layout_height_police = 80;
    int score=0;
    RelativeLayout relativeLayout;
    TextView score_LBL ;
    Random random = new Random();
    ArrayList<ImageView> PoliceQ;
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d("kkkkkkk", "onPause: ");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d("kkkkkk", "onResume: ");
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        heartArry = new ImageView[]{
                findViewById(R.id.heart1),
                findViewById(R.id.heart2),
                findViewById(R.id.heart3)
        };
        relativeLayout = findViewById(R.id.relativeLayout);
        display = getWindowManager().getDefaultDisplay();// find screen size X , Y
        size = new Point();
        display.getSize(size);
        sizeX = size.x;
        sizeY = size.y;
        left_BTN = findViewById(R.id.left_BTN);
        right_BTN = findViewById(R.id.right_BTN);
//        red_car = findViewById(R.id.red_car);
        score_LBL = findViewById(R.id.score_INT);
        PoliceQ = new ArrayList<ImageView>();
        left_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLeft();
            }
        });
        right_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRight();
            }
        });
        red_car = new ImageView(this);
        initRedCar(red_car);
        gameLoop();
        secondHendler();
    }
    public void initRedCar(ImageView red_car) {        red_car.setImageResource(R.drawable.car);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.MATCH_PARENT);
        params.height = convertDPToInt(dp_layout_height_police);
        params.weight = convertDPToInt(dp_layout_width_police);
        relativeLayout.addView(red_car,params);
        red_car.setX((sizeX/gamePaths)*-1);
        red_car.setY(sizeY-red_car.getHeight()-300);
        red_car.setVisibility(View.VISIBLE);
    }
    public int convertDPToInt(int dp){
        Resources r = getResources();
         return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp,r.getDisplayMetrics()));
    }
    public void addPolice() {
        imageView_police = new ImageView(this);
        imageView_police.setImageResource(R.drawable.police);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.MATCH_PARENT);
        params.height = convertDPToInt(dp_layout_height_police);
        params.weight = convertDPToInt(dp_layout_width_police);
        int rand_col =  random.nextInt(3)-1; //random number to set the police
        relativeLayout.addView(imageView_police,params);
        imageView_police.setX((sizeX/gamePaths)*rand_col);
        imageView_police.setY(0);
        imageView_police.setVisibility(View.VISIBLE);
        PoliceQ.add(imageView_police);
    }
    public void gameLoop(){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameLoop();
                addPolice();
                getScore();
            }
        }, (1000 + random.nextInt(4000)));
    }
    public void secondHendler(){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                secondHendler();
                changeThePolice();
                checkIfClash();
            }
        }, 50);
    }

    public void checkIfClash(){
    if(!PoliceQ.isEmpty() ){
        System.out.println("Police X " + PoliceQ.get(0).getX());
        System.out.println("car X " + red_car.getX());
    if (PoliceQ.get(0).getX() == red_car.getX() && PoliceQ.get(0).getY() >= red_car.getY() - 10) {
        removeHeart();
    }}
    }
    public void getScore(){
        score+=5;
        score_LBL.setText(""+score);
    }

    public void clickLeft() {
        score++;
    if (0 < red_car.getX() - (sizeX / gamePaths))
            red_car.setX(red_car.getX() - (sizeX / gamePaths));
    }

    public void clickRight() {
        score++;
        if (sizeX > red_car.getX() + (sizeX / gamePaths))
            red_car.setX(red_car.getX() + (sizeX / gamePaths));
    }

    public void changeThePolice(){
        for(int i=0;i<PoliceQ.size();i++){
            if(PoliceQ.get(i) != null){
                if (PoliceQ.get(i).getY() + 10 < (red_car.getY())) {
            PoliceQ.get(i).setY(PoliceQ.get(i).getY() + 10);
        }else{
            score+=2;
            PoliceQ.get(i).setImageResource(0);
            PoliceQ.remove(i);}
        }}
    }

    public void removeHeart() {
            if (countLife==1)
                gameOver();
            if (countLife==2){
                countLife--;
                heartArry[1].setVisibility(View.INVISIBLE);}
            if (countLife == 3){
                countLife--;
                heartArry[0].setVisibility(View.INVISIBLE);}
    }

    public void gameOver() {
        Intent intent = new Intent(this, EndGameScreen.class);
        intent.putExtra("SCORE",score);
        startActivity(intent);

    }

}