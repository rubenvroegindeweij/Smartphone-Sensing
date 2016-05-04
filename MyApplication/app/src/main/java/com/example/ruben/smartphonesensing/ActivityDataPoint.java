package com.example.ruben.smartphonesensing;

/**
 * Created by Ruben on 4-5-2016.
 */
public class ActivityDataPoint implements Comparable<ActivityDataPoint>{
    private double meanAcceleration;
    private double maxMinDifference;
    public String activityLabel;
    private double currentMean;
    private double currentDiff;
    public ActivityDataPoint(double mean, double diff, String label) {
        meanAcceleration = mean;
        maxMinDifference = diff;
        activityLabel = label;
        currentMean = 0;
        currentDiff = 0;
    }

    public void SetCurrentSituation(double mean, double diff){
        currentMean = mean;
        currentDiff = diff;
    }

    public double Distance(){
        double meanDiff = currentMean - meanAcceleration;
        double diffDiff = currentDiff - maxMinDifference;
        double distance = Math.sqrt(meanDiff*meanDiff+diffDiff*diffDiff);
        return distance;
    }

    public int compareTo(ActivityDataPoint other) {
        double result = this.Distance()-other.Distance();
        if(result > 0d)
            return 1;
        else if(result < 0d)
            return -1;
        else
            return 0;
    }
}
