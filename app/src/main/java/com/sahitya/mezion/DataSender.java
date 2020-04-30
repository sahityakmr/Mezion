package com.sahitya.mezion;

import android.util.Log;

import com.google.gson.Gson;
import com.sahitya.mezion.model.SensorData;

import java.io.DataOutputStream;
import java.net.Socket;

public class DataSender extends Thread {

    private static final String TAG = "DataSender";
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private SensorData sensorData;
    private boolean isSending = false;
    private boolean isSet = false;
    private Gson gson;

    DataSender() {
        gson = new Gson();
    }

    void stopSending() {
        isSending = false;
        Log.d(TAG, "stopSending: ");
    }

    void setSensorData(SensorData sensorData) {
        this.sensorData = sensorData;
        isSet = true;
        Log.d(TAG, "setReading: ");
    }

    @Override
    public void run() {
        super.run();
        try {
            socket = new Socket("192.168.43.50", 2536);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            isSending = true;
            while (isSending) {
                if(isSet) {
                    String str;
                    str = gson.toJson(sensorData.getAccelerometerReading());
                    dataOutputStream.writeUTF(str);
                    str = gson.toJson(sensorData.getGyroscopeReading());
                    dataOutputStream.writeUTF(str);
                    str = gson.toJson(sensorData.getMagnetometerReading());
                    dataOutputStream.writeUTF(str);
                    isSet = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
