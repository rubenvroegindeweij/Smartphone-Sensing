package com.example.ruben.smartphonesensing;

/**
 * Created by Ruben on 4-5-2016.
 */
public class ActivityDataPoint implements Comparable<ActivityDataPoint>{
    private double meanAcceleration;
    private double maxMinDifference;
    public String activityLabel;
    private ActivityAnalyzer activityAnalyzer;

    public ActivityDataPoint(double mean, double diff, String label, ActivityAnalyzer activityAnalyzer) {
        meanAcceleration = mean;
        maxMinDifference = diff;
        activityLabel = label;
        this.activityAnalyzer = activityAnalyzer;
    }

    public double Distance(){
        double meanDiff = activityAnalyzer.currentMeanAcceleration - meanAcceleration;
        double diffDiff = activityAnalyzer.currentMaxMinDifference - maxMinDifference;
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

    @Override
    public String toString(){
        return activityLabel;
    }
}
