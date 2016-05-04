package com.example.ruben.smartphonesensing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private WifiManager wifiManager;

    private float aX = 0;
    private float aY = 0;
    private float aZ = 0;
    private double totalAcceleration = 0;
    private double minA = Double.MAX_VALUE;
    private double maxA = Double.MIN_VALUE;
    private double sum = 0;
    private double amount = 0;
    private boolean runWindows = false;
    private Handler handler = null;
    private ArrayList<ActivityDataPoint> activityDataPoints = new ArrayList<ActivityDataPoint>();

    private TextView currentX, currentY, currentZ, titleAcc, textRssi, acceleration, activity;
    private CheckBox train;
    private RadioButton trainStanding, trainWalking;

    private WifiInfo wifiInfo;
    Button buttonRssi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);
        titleAcc = (TextView) findViewById(R.id.titleAcc);
        textRssi = (TextView) findViewById(R.id.textRSSI);
        buttonRssi = (Button) findViewById(R.id.buttonRSSI);
        acceleration = (TextView) findViewById(R.id.acceleration);
        activity = (TextView) findViewById(R.id.activity);
        train = (CheckBox) findViewById(R.id.train);
        trainStanding = (RadioButton) findViewById(R.id.trainStanding);
        trainWalking = (RadioButton) findViewById(R.id.trainWalking);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);

            runWindows = true;
            handler = new Handler();
            final Runnable r = new Runnable(){
                @Override
                public void run(){
                    //if(maxA - minA > 4)
                    //    activity.setText("Walking");
                    //else
                    //    activity.setText("Standing");
                    //acceleration.setText(Double.toString(maxA-minA));
                    System.out.println("In loop");
                    double meanAcceleration = sum/amount;
                    double maxMinDifference = maxA-minA;
                    String label = "";
                    if(train.isChecked() && (trainStanding.isChecked() || trainWalking.isChecked())){
                        if(trainStanding.isChecked())
                            label = "Standing";
                        if(trainWalking.isChecked())
                            label = "Walking";
                        ActivityDataPoint adp = new ActivityDataPoint(meanAcceleration, maxMinDifference, label);
                        activityDataPoints.add(adp);
                        System.out.println("Add training point");
                    }
                    else{
                        for(ActivityDataPoint adp: activityDataPoints){
                            adp.SetCurrentSituation(meanAcceleration, maxMinDifference);
                        }
                        Collections.sort(activityDataPoints);
                        int K = 5;
                        if (activityDataPoints.size() >= K){
                            System.out.println("Enough training points");
                            int standingPoints = 0;
                            int walkingPoints = 0;

                            for(int i = 0; i < K; i++) {
                                if(activityDataPoints.get(i).activityLabel.equals("Standing"))
                                    standingPoints++;
                                if(activityDataPoints.get(i).activityLabel.equals("Walking"))
                                    walkingPoints++;
                            }

                            if(standingPoints > walkingPoints)
                                activity.setText("Standing");
                            else if(standingPoints < walkingPoints)
                                activity.setText("Walking");
                        }
                    }

                    sum = 0;
                    amount = 0;
                    minA = Double.MAX_VALUE;
                    maxA = Double.MIN_VALUE;
                    if(runWindows) {
                        handler.postDelayed(this, 1000);
                    }
                }
            };

            handler.postDelayed(r, 1000);

        } else {
            // No accelerometer!
        }

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        buttonRssi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                wifiInfo = wifiManager.getConnectionInfo();
                textRssi.setText("\n\tSSID = " + wifiInfo.getSSID()
                        + "\n\tRSSI = " + wifiInfo.getRssi()
                        + "\n\tLocal Time = " + System.currentTimeMillis());
            }
        });
    }

    // onResume() register the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    // onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
        //acceleration.setText("0.0");

        // get the the x,y,z values of the accelerometer
        aX = event.values[0];
        aY = event.values[1];
        aZ = event.values[2];

        // display the current x,y,z accelerometer values
        currentX.setText(Float.toString(aX));
        currentY.setText(Float.toString(aY));
        currentZ.setText(Float.toString(aZ));
        totalAcceleration = Math.sqrt((double)(aX*aX+aY*aY+aZ*aZ));
        //acceleration.setText(Double.toString(totalAcceleration));

        if(runWindows) {
            if (totalAcceleration > maxA)
                maxA = totalAcceleration;
            if (totalAcceleration < minA)
                minA = totalAcceleration;
            sum += totalAcceleration;
            amount++;
        }

        if ((Math.abs(aX) > Math.abs(aY)) && (Math.abs(aX) > Math.abs(aZ))) {
            titleAcc.setTextColor(Color.RED);
        }
        if ((Math.abs(aY) > Math.abs(aX)) && (Math.abs(aY) > Math.abs(aZ))) {
            titleAcc.setTextColor(Color.BLUE);
        }
        if ((Math.abs(aZ) > Math.abs(aY)) && (Math.abs(aZ) > Math.abs(aX))) {
            titleAcc.setTextColor(Color.GREEN);
        }
    }

}

/*public class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }
}*/