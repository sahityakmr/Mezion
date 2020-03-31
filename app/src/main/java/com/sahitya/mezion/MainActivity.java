package com.sahitya.mezion;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.sahitya.mezion.model.Reading;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private Sensor accelerometerSensor;
    private Sensor magnetometerSensor;
    private DataSender dataSender;
    private boolean isPushSwitchEnabled;
    private boolean isListenSwitchEnabled;
    private Switch listenSwitch;
    private Switch dumpSwitch;
    private Switch pushSwitch;
    private Reading reading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSender = new DataSender();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        initViews();
        initSensors();
        addListeners();

        // logAllSensors();
        // logRequiredSensorAvailability();

    }

    private void addListeners() {
        listenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isListenSwitchEnabled = isChecked;
                if(isListenSwitchEnabled)
                    registerListeners();
                else
                    unregisterListener();
            }
        });

        pushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPushSwitchEnabled = isChecked;
                if(isPushSwitchEnabled) {
                    dataSender = new DataSender();
                    dataSender.start();
                }
                else
                    dataSender.stopSending();
            }
        });
    }

    private void initViews() {
        listenSwitch = findViewById(R.id.listen_btn);
        dumpSwitch = findViewById(R.id.dump_btn);
        pushSwitch = findViewById(R.id.push_btn);
    }

    private void initSensors() {
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    private void logRequiredSensorAvailability() {
        Log.d(TAG, "check Accelerometer: " + checkAccelerometer());
        Log.d(TAG, "check Gyroscope: " + checkGyroscope());
        Log.d(TAG, "check Magnetometer: " + checkMagnetometer());
    }

    private boolean checkMagnetometer() {
        return magnetometerSensor != null;
    }

    private boolean checkGyroscope() {
        return gyroscopeSensor != null;
    }

    private boolean checkAccelerometer() {
        return accelerometerSensor != null;
    }

    private void logAllSensors() {
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor : sensorList)
            Log.d(TAG, "sensor name : " + sensor.getName());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "onSensorChanged: " + event.sensor.getName());
        Sensor sensor = event.sensor;
        switch (sensor.getType()){
            case Sensor.TYPE_GYROSCOPE:
                // Log.d(TAG, "Gyroscope readings : " + Arrays.toString(event.values) + " at : " + event.timestamp);
                reading.setGyroReading(event.values);
                break;
            case Sensor.TYPE_ACCELEROMETER:
                // Log.d(TAG, "Accelerometer readings : " + Arrays.toString(event.values) + " at : " + event.timestamp);
                reading.setAccReading(event.values);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                // Log.d(TAG, "Magnetic readings : " + Arrays.toString(event.values) + " at : " + event.timestamp);
                reading.setMagReading(event.values);
                break;
        }

        if(isPushSwitchEnabled){
            dataSender.setReading(reading);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterListener();
    }

    private void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListeners();
    }

    private void registerListeners(){
        if(isListenSwitchEnabled) {
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            reading = new Reading();
        }
    }
}
