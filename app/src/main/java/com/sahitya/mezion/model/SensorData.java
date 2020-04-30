package com.sahitya.mezion.model;

import lombok.Data;

@Data
public class SensorData {
    private Reading magnetometerReading;
    private Reading accelerometerReading;
    private Reading gyroscopeReading;
}
