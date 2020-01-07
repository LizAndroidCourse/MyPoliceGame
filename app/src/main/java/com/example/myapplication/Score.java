package com.example.myapplication;

import android.location.Location;
import android.util.Log;

import java.util.Comparator;

public class Score {
    int score;
    String name;
    double latitude;
    double longitude;


    public Score() {
        score = 0;
        name = "none";
    }

    public void setLat(double latitude){
        this.latitude = latitude;
    }
    public void setLng(double longitude){
        this.longitude = longitude;
    }
    public int getScore(){
        return score;
    }
    public String getName(){
        return name;
    }



    public static class CustomComparator implements Comparator<Score> {
        @Override
        public int compare(Score o1, Score o2) {
            if(o1.getScore() < o2.getScore()){

                return 1;

            } else if(o1.getScore() == o2.getScore()){

                return 0;

            }

            return -1;

        }
    }
    @Override
    public String toString(){
        return "Score: "+score + " | Name: "+ name ;
    }
}