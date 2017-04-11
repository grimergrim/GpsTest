package com.iavorskyi.gpstest.utils;

import android.os.Environment;
import android.util.Log;

import com.iavorskyi.gpstest.entities.GpsEntity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {

    private final static String MAIN_FOLDER_NAME = "NewGpsTracking";
    private final static String COORDINATES_FOLDER = "coordinates";
    private final static String REPORTS_FOLDER_NAME = "reports";
    private final static String SUCCESS_FILE_NAME = "success.txt";
    private final static String ERRORS_FILE_NAME = "errors.txt";
    private final static String TIME = "time:";
    private final static String LATITUDE = ";lat:";
    private final static String LONGITUDE = ";lon:";
    private final static String ACCURACY = ";accuracy:";
    private final static String SPEED = ";speed:";

    private TimeAndDateUtils mTimeAndDateUtils;

    public FileUtils() {
        mTimeAndDateUtils = new TimeAndDateUtils();
    }

    public Map<String, List<GpsEntity>> readFileToSend() {
        Map<String, List<GpsEntity>> gpsEntityMap = new HashMap<>();
        File directoryWithCoordinates = new File(Environment.getExternalStorageDirectory() + "/"
                + MAIN_FOLDER_NAME, COORDINATES_FOLDER);
        if (directoryWithCoordinates.exists()) {
            File[] listFiles = directoryWithCoordinates.listFiles();
            for (int i = 0; i < listFiles.length - 1; i++) {
                File file = listFiles[i];
                List<GpsEntity> gpsEntityList = readGpsEntitiesFromFile(file);
                gpsEntityMap.put(file.getName(), gpsEntityList);
            }
        }
        return gpsEntityMap;
    }

    private List<GpsEntity> readGpsEntitiesFromFile(File file) {
        FileInputStream fileInputStream;
        List<GpsEntity> gpsEntityList = new ArrayList<>();
        try {
            fileInputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                GpsEntity gpsEntity = new GpsEntity();
                String[] params = line.split(";");
                for (String param : params) {
                    String[] parameter = param.split(":");
                    String key = parameter[0];
                    switch (key) {
                        case "lat": gpsEntity.setLatitude(Double.valueOf(parameter[1]));
                            break;
                        case "lon": gpsEntity.setLongitude(Double.valueOf(parameter[1]));
                            break;
                        case "speed": gpsEntity.setSpeed(Double.valueOf(parameter[1]));
                            break;
                        case "time": gpsEntity.setTime(Long.valueOf(parameter[1]));
                            break;
                        case "accuracy": gpsEntity.setAccuracy(Double.valueOf(parameter[1]));
                            break;
                    }
                }
                gpsEntityList.add(gpsEntity);
            }
            reader.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gpsEntityList;
    }


    public void writeErrorToFile(String time, String errorText, String details) {
        //TODO add gsm and gps accuracy and other from amt project
        PrintWriter out = null;
        try {
            File path = new File(Environment.getExternalStorageDirectory() + "/"
                    + MAIN_FOLDER_NAME, REPORTS_FOLDER_NAME);
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path, ERRORS_FILE_NAME);
            out = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true)));
            out.println(mTimeAndDateUtils.getDateAsStringFromSystemTime(System.currentTimeMillis()) + " " + errorText + " " + details);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    public void deleteSandedData(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + MAIN_FOLDER_NAME + "/" + COORDINATES_FOLDER + "/" + fileName);
        if (file.exists()) {
            boolean delete = file.delete();
            Log.e("=============", "deleting: " + fileName + " " + delete);
        } else {
            Log.e("=============", "file does not exist: " + fileName);
        }
    }

    public void writeThatDataWasSanded(String fileName) {
        PrintWriter out = null;
        try {
            File path = new File(Environment.getExternalStorageDirectory() + "/"
                    + MAIN_FOLDER_NAME, REPORTS_FOLDER_NAME);
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path, SUCCESS_FILE_NAME);
            out = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true)));
            out.println(mTimeAndDateUtils.getDateAsStringFromSystemTime(System.currentTimeMillis()) + " " + fileName + " was relieved by server");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
        Log.e("=============", "wrote report: " + fileName);
    }

    public void writeGpsToFile(GpsEntity gpsEntity, String fileName) {
        PrintWriter out = null;
        try {
            File path = new File(Environment.getExternalStorageDirectory() + "/"
                    + MAIN_FOLDER_NAME, COORDINATES_FOLDER);
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path, fileName);
            out = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true)));
            //TODO change time to amt format
            out.println(TIME + gpsEntity.getTime() + LATITUDE + gpsEntity.getLatitude()
                    + LONGITUDE + gpsEntity.getLongitude() + ACCURACY + gpsEntity.getAccuracy()
                    + SPEED + gpsEntity.getSpeed());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    public String getNewFileName() {
        return mTimeAndDateUtils.getCurrentTimeForFileName() + ".txt";
    }

}
