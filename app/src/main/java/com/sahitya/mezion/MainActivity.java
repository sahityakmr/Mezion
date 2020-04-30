package com.sahitya.mezion;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;

import com.sahitya.mezion.model.Reading;
import com.sahitya.mezion.model.SensorData;

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
    private boolean isDumpSwitchEnabled;
    private boolean isShowSwitchEnabled;
    private TextView magnetometerView;
    private TextView gyroscopeView;
    private TextView accelerometerView;
    private Switch listenSwitch;
    private Switch dumpSwitch;
    private Switch pushSwitch;
    private Switch showSwitch;
    private SensorData sensorData;

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

    private void initViews() {
        listenSwitch = findViewById(R.id.listen_btn);
        dumpSwitch = findViewById(R.id.dump_btn);
        pushSwitch = findViewById(R.id.push_btn);
        showSwitch = findViewById(R.id.show_btn);
        gyroscopeView = findViewById(R.id.gyroReads);
        magnetometerView = findViewById(R.id.magReads);
        accelerometerView = findViewById(R.id.accReads);
    }

    private void initSensors() {
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    private void addListeners() {
        listenSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isListenSwitchEnabled = isChecked;
            if (isListenSwitchEnabled)
                registerListeners();
            else
                unregisterListener();
        });

        pushSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isPushSwitchEnabled = isChecked;
            if (isPushSwitchEnabled) {
                dataSender = new DataSender();
                dataSender.start();
            } else
                dataSender.stopSending();
        });

        showSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> isShowSwitchEnabled = isChecked);

        dumpSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> isDumpSwitchEnabled = isChecked));
    }

    private void registerListeners() {
        if (isListenSwitchEnabled) {
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorData = new SensorData();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        switch (sensor.getType()) {
            case Sensor.TYPE_GYROSCOPE:
                sensorData.setGyroscopeReading(new Reading(event.values));
                break;
            case Sensor.TYPE_ACCELEROMETER:
                sensorData.setAccelerometerReading(new Reading(event.values));
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                sensorData.setMagnetometerReading(new Reading(event.values));
                break;
        }

        if (isPushSwitchEnabled)
            dataSender.setSensorData(sensorData);
        if (isShowSwitchEnabled)
            displayReadings();
        if (isDumpSwitchEnabled)
            Log.d(TAG, "onSensorChanged: " + sensorData);
    }

    private void displayReadings() {
        accelerometerView.setText(sensorData.getAccelerometerReading().toString());
        gyroscopeView.setText(sensorData.getGyroscopeReading().toString());
        magnetometerView.setText(sensorData.getMagnetometerReading().toString());
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

    private void logAllSensors() {
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensorList)
            Log.d(TAG, "sensor name : " + sensor.getName());
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
}
