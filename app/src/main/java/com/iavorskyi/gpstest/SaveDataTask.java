package com.iavorskyi.gpstest;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class SaveDataTask extends AsyncTask<Entity, Void, Void> {

    private final static String FOLDER_NAME = "A1NewGpsTracking";
    private final static String FILE_NAME = "coordinates.txt";
    private final static String LATITUDE = " lat: ";
    private final static String LONGITUDE = " lon: ";

    protected Void doInBackground(Entity... entities) {
        saveDateToFile(entities[0]);
        return null;
    }

    private void saveDateToFile(Entity entity) {
        PrintWriter out = null;
        try {
            File path = new File(Environment.getExternalStorageDirectory() + "/", FOLDER_NAME);
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path, FILE_NAME);
            out = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true)));
            out.println(entity.getTime() + LATITUDE + entity.getLatitude() + LONGITUDE + entity.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

}
