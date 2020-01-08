package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.math.MathContext;
import java.util.ArrayList;
import java.util.Random;

public class Game extends AppCompatActivity implements SensorEventListener {

    //view objects
    private Button right_BTN;
    private Button left_BTN;
    private ImageView[] heartArry;
    private ImageView red_car;
    private ImageView imageView_police;
    private ImageView imageView_coin;
    private TextView score_LBL;
    final private int MANUAL_MODE = 1 ;
    // screen size parameters
    int sizeX, sizeY;
    Display display;
    Handler handler;
    Point size;
    RelativeLayout relativeLayout;
    //sensor paramters
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    SensorManager moving_controller;
    Sensor sensor;
    //game utilities
    int gamePaths = 5;
    Runnable game_runnable;
    Runnable init_police_runnable;
    int dp_layout_height_police = 80;
    int score = 1;
    int policeSpeed = 10;
    int policeRatio ;
    int countLife = 3;
    int mode;
    int widthPath;
    boolean is_game_stopped = false;
    Random random = new Random();
    ArrayList<ImageView> policeQ;
    ArrayList<ImageView> coinQ;
    MediaPlayer mediaPlayerCrash;
    MediaPlayer mediaPlayerCoin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initVars();
        initPolices();
        gameLoop();
        mode = getIntent().getIntExtra("MODE",MANUAL_MODE);
        if(mode == MANUAL_MODE){
            manualMode();
        }else{
            sensorMode();
        }

    }

    public void sensorMode(){
        left_BTN.setVisibility(View.INVISIBLE);
        right_BTN.setVisibility(View.INVISIBLE);
        //declaring Sensor Manager and sensor type
        moving_controller = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = moving_controller.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        moving_controller.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }
    public void manualMode(){
        policeRatio = 800;
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
    }
    public void initVars() {
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
        widthPath = sizeX/gamePaths;
        left_BTN = findViewById(R.id.left_BTN);
        right_BTN = findViewById(R.id.right_BTN);
        score_LBL = findViewById(R.id.score_INT);
        policeQ = new ArrayList<>();
        coinQ = new ArrayList<>();
        red_car = new ImageView(this);
        initRedCar(red_car);
       mediaPlayerCrash = MediaPlayer.create(getApplicationContext(), R.raw.crash_sound);
        mediaPlayerCoin = MediaPlayer.create(getApplicationContext(), R.raw.coin_sound);

    }


    public void initRedCar(ImageView red_car) {
        red_car.setImageResource(R.drawable.car);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.height = convertDPToInt(dp_layout_height_police);
       red_car.setLayoutParams(params);
        params.width = sizeX / gamePaths;
        relativeLayout.addView(red_car, params);
        red_car.setX(sizeX / gamePaths);
        red_car.setY(sizeY - red_car.getHeight() - 300);
        red_car.setVisibility(View.VISIBLE);

    }

    public void addPolice() {
        imageView_police = new ImageView(this);
        imageView_police.setImageResource(R.drawable.police);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.height = convertDPToInt(dp_layout_height_police);
        params.width = sizeX / gamePaths;
        int rand_col = random.nextInt(gamePaths);//random number to set the police
        if (!policeQ.isEmpty()) {// Check that the police car does not leave one after the other in the same row
            while ((((sizeX / gamePaths) * rand_col) == (int) policeQ.get(policeQ.size() - 1).getX()) ||
                    ((((sizeX / gamePaths) * rand_col) == (int) policeQ.get(policeQ.size() - 1).getY()))) {
                rand_col = random.nextInt(3);
            }
        }
        relativeLayout.addView(imageView_police, params);
        imageView_police.setX((sizeX / gamePaths) * rand_col);
        imageView_police.setY(0);
        imageView_police.setVisibility(View.VISIBLE);
        policeQ.add(imageView_police);
    }
    public void addCoin() {
        imageView_coin = new ImageView(this);
        imageView_coin.setImageResource(R.drawable.coin);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.height = convertDPToInt(dp_layout_height_police);
        params.width = sizeX / gamePaths;
        int rand_col = random.nextInt(gamePaths);//random number to set the police
        if (!coinQ.isEmpty()) {// Check that the police car does not leave one after the other in the same row
            while ((((sizeX / gamePaths) * rand_col) == (int) coinQ.get(coinQ.size() - 1).getX()) ||
                    ((((sizeX / gamePaths) * rand_col) == (int) coinQ.get(coinQ.size() - 1).getY()))) {
                rand_col = random.nextInt(3);
            }
        }
        relativeLayout.addView(imageView_coin, params);
        imageView_coin.setX((sizeX / gamePaths) * rand_col);
        imageView_coin.setY(0);
        imageView_coin.setVisibility(View.VISIBLE);
        coinQ.add(imageView_coin);
    }

    public int convertDPToInt(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void initPolices() {
        handler = new Handler();
        init_police_runnable = new Runnable() {
            @Override
            public void run() {
                initPolices();
                Random randomActor = new Random();
                int random_int = randomActor.nextInt(2);
                if(random_int == 1){
                    addPolice();
                }
                else{
                    addCoin();
                }
                setScore();
            }
        };
        if (!is_game_stopped)
            handler.postDelayed(init_police_runnable, (policeRatio + random.nextInt(1000)));
    }

    public void gameLoop() {
        handler = new Handler();
        game_runnable = new Runnable() {
            @Override
            public void run() {
                gameLoop();
                actorsPosition();
                checkIfClash();
            }
        };
        if (!is_game_stopped) {
            if (score % 10 < 3) {
                setScore();
                policeSpeed += 1;
                if (policeRatio < 0) {
                    policeRatio -= 80;
                }
            }
            handler.postDelayed(game_runnable, 30);
        }
    }

    public void checkIfClash() {
        if (!policeQ.isEmpty()) {
            if (policeQ.get(0).getX() == red_car.getX() && policeQ.get(0).getY() >= red_car.getY() - red_car.getHeight()) {
                removeHeart();
                //   mediaPlayerCrash.start();             }
                policeQ.get(0).setVisibility(View.INVISIBLE);
                policeQ.remove(0);
            }
        }
        if (!coinQ.isEmpty()){
            if (coinQ.get(0).getX() == red_car.getX() && coinQ.get(0).getY() >= red_car.getY() - red_car.getHeight()) {
                score+=5;
                //mediaPlayerCoin.start();
                coinQ.get(0).setVisibility(View.INVISIBLE);
                coinQ.remove(0);

            }   }
    }

    public void setScore() {
        score += 1;
        score_LBL.setText("" + score);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float u = event.values[0];
        float v = event.values[1];
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float)Math.sqrt(x*x + y*y + z*z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;}

        if (Math.abs(u) > Math.abs(v)) {
            if (u > -10 && u < -6) {
                red_car.setX(4*widthPath);
            }
            if (u > -6 && u < -2) {
                red_car.setX(3*widthPath);
            }
            if(u > -2 && u < 2){
                red_car.setX(2*widthPath);
            }
            if (u>2 && u<6){
                red_car.setX(widthPath);
            }
            if(u>6&& u<10){
                red_car.setX(0);
            }
        }
    }

    public void clickLeft() {
        score++;
        if (red_car.getX() > 0)
            red_car.setX(red_car.getX() - (sizeX / gamePaths));
    }

    public void clickRight() {
        score++;
        if (red_car.getX() < sizeX - red_car.getWidth())
            red_car.setX(red_car.getX() + (sizeX / gamePaths));
    }

    public void actorsPosition() {
        for (int i = 0; i < policeQ.size(); i++) {
            if (policeQ.get(i) != null) {
                if (policeQ.get(i).getY() + 10 < (red_car.getY())) {
                    policeQ.get(i).setY(policeQ.get(i).getY() + policeSpeed);
                } else {
                    score += 2;
                    score_LBL.setText("" + score);
                    policeQ.get(i).setImageResource(0);
                    policeQ.remove(i);
                }
            }
        }
        for (int i = 0; i < coinQ.size(); i++) {
            if (coinQ.get(i) != null) {
                if (coinQ.get(i).getY() + 10 < (red_car.getY())) {
                    coinQ.get(i).setY(coinQ.get(i).getY() + policeSpeed);
                } else {
                    score += 2;
                    score_LBL.setText("" + score);
                    coinQ.get(i).setImageResource(0);
                    coinQ.remove(i);
                }
            }
        }
    }

    public void removeHeart() {
        if (countLife == 1) {
            is_game_stopped = true;
            moveToGameOverScreen();
        }
        if (countLife == 2) {
            countLife--;
            heartArry[1].setVisibility(View.INVISIBLE);
        }
        if (countLife == 3) {
            countLife--;
            heartArry[0].setVisibility(View.INVISIBLE);
        }
    }

    public void moveToGameOverScreen() {
        Intent intent = new Intent(this, EndGameScreen.class);
        intent.putExtra("SCORE", score);
        startActivity(intent);
        this.finish();

    }

    @Override
    protected void onPause() {
        is_game_stopped = true;
        //unregister Sensor listener
        if(mode != MANUAL_MODE) {
            moving_controller.unregisterListener(this, sensor);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (is_game_stopped) {
            is_game_stopped = false;
            if(mode!=MANUAL_MODE) {
                moving_controller.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
            }
            initPolices();
            gameLoop();
        }
    }
}