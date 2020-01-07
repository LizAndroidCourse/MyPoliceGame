package com.example.myapplication;

import java.util.ArrayList;
import java.util.Collections;

public class ScoresList {
    public static int MAX_SCORES_COUNT = 10;
    private ArrayList<Score> records = new ArrayList<>();

    public void sortRecords(){
        Collections.sort(records,new Score.CustomComparator());
    }

    public ArrayList<Score> getRecords(){
        return records;
    }

    private int getMinIndex(){
        int minIndex = 0;
        for(int i = 0; i < records.size(); i++){
            if(records.get(i).getScore() < records.get(minIndex).getScore()){
                minIndex = i;
            }
        }
        return minIndex;
    }

    public boolean isTopTen(Score score){
        return (records.size() < MAX_SCORES_COUNT) || (score.getScore() >= records.get(getMinIndex()).getScore());
    }

    public void addToTopTen(Score score){
        if(records.size() == MAX_SCORES_COUNT){
            records.remove(getMinIndex());
        }
        records.add(score);
    }

}