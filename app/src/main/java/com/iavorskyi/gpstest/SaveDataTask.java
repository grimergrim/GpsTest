package com.iavorskyi.gpstest;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class SaveDataTask extends AsyncTask<Entity, Void, Void> {

    protected Void doInBackground(Entity... entites) {
        //TODO save in file. Set flag file saving. Baybe.
        saveDateToFile(entites[0]);
        return null;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Long result) {

    }

    private void saveDateToFile(Entity entity) {
        try {
            File path = new File(Environment.getExternalStorageDirectory() + "/" + "Aaabbbcccddd", "coordinates");
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path, "Coordinates.txt");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true)));
            out.println(entity.getTime() + " latitude: " + entity.getLatitude() + " longitude: " + entity.getLongitude());
            out.close();
        } catch (IOException e) {
            //TODO lovim excepsheny
            e.printStackTrace();
        }
    }
}
