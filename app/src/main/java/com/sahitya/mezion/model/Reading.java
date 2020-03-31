package com.sahitya.mezion.model;

public class Reading {
    public static final int SENSOR_MAGNETOMETER = 1;
    public static final int SENSOR_ACCELEROMETER = 2;
    public static final int SENSOR_GYROSCOPE = 3;
    private int sensorType = -1;
    private Reading accReading;
    private Reading magReading;
    private Reading gyroReading;
    private float x = 0;
    private float y = 0;
    private float z = 0;

    public Reading() {
        accReading = new Reading(SENSOR_ACCELEROMETER);
        magReading = new Reading(SENSOR_MAGNETOMETER);
        gyroReading = new Reading(SENSOR_GYROSCOPE);
    }

    public Reading(int sensorType) {
        this.sensorType = sensorType;
    }

    public Reading getAccReading() {
        return accReading;
    }

    public void setAccReading(float[] values) {
        this.accReading.x = values[0];
        this.accReading.y = values[1];
        this.accReading.z = values[2];
    }

    public Reading getMagReading() {
        return magReading;
    }

    public void setMagReading(float[] values) {
        this.magReading.x = values[0];
        this.magReading.y = values[1];
        this.magReading.z = values[2];
    }

    public Reading getGyroReading() {
        return gyroReading;
    }

    public void setGyroReading(float[] values) {
        this.gyroReading.x = values[0];
        this.gyroReading.y = values[1];
        this.gyroReading.z = values[2];
    }
}
