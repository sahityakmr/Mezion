package com.sahitya.mezion;

import android.util.Log;

import com.google.gson.Gson;
import com.sahitya.mezion.model.Reading;

import java.io.DataOutputStream;
import java.net.Socket;

public class DataSender extends Thread {

    private static final String TAG = "DataSender";
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private Reading reading;
    private boolean isSending = false;
    private boolean isSet = false;
    private Gson gson;

    public DataSender() {
        gson = new Gson();
    }

    public void stopSending() {
        isSending = false;
        Log.d(TAG, "stopSending: ");
    }

    public void setReading(Reading reading) {
        this.reading = reading;
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
                    str = gson.toJson(reading.getAccReading());
                    dataOutputStream.writeUTF(str);
                    str = gson.toJson(reading.getGyroReading());
                    dataOutputStream.writeUTF(str);
                    str = gson.toJson(reading.getMagReading());
                    dataOutputStream.writeUTF(str);
                    isSet = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
