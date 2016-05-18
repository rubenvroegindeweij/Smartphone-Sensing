package com.example.ruben.smartphonesensing;

import android.os.Handler;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ruben on 10-5-2016.
 */
public class ActivityAnalyzer {
    private ArrayList<ActivityDataPoint> activityDataPoints;
    public double currentMeanAcceleration;
    public double currentMaxMinDifference;

    public ActivityAnalyzer(){
        activityDataPoints = new ArrayList<ActivityDataPoint>();
        currentMeanAcceleration = 0;
        currentMaxMinDifference = 0;
    }

    public void addNewActivityDataPoint(double meanAcceleration, double maxMinDifference, String label){
        ActivityDataPoint adp = new ActivityDataPoint(meanAcceleration, maxMinDifference, label, this);
        activityDataPoints.add(adp);
    }

    public void setCurrentSituation(double meanAcceleration, double maxMinDifference){
        currentMeanAcceleration = meanAcceleration;
        currentMaxMinDifference = maxMinDifference;
    }

    public String getActivity(int K){
        Collections.sort(activityDataPoints);
        if (activityDataPoints.size() >= K){
            int standingPoints = 0;
            int walkingPoints = 0;

            for(int i = 0; i < K; i++) {
                if(activityDataPoints.get(i).activityLabel.equals("Standing"))
                    standingPoints++;
                if(activityDataPoints.get(i).activityLabel.equals("Walking"))
                    walkingPoints++;
            }

            if(standingPoints > walkingPoints)
                return "Standing";
            else if(standingPoints < walkingPoints)
                return "Walking";
        }
        return null;
    }
}
