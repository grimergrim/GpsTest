package com.iavorskyi.gpstest.utils;

import android.os.Environment;

import com.iavorskyi.gpstest.entities.GpsEntity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {

    private final static String MAIN_FOLDER_NAME = "NewGpsTracking";
    private final static String COORDINATES_FOLDER = "coordinates";
    private final static String REPORTS_FOLDER_NAME = "reports";
    private final static String SUCCESS_FILE_NAME = "success.txt";
    private final static String ERRORS_FILE_NAME = "errors.txt";
    private final static String TIME = "time: ";
    private final static String LATITUDE = " lat: ";
    private final static String LONGITUDE = " lon: ";
    private final static String ACCURACY = " accuracy: ";
    private final static String SPEED = " speed: ";

    private TimeAndDateUtils mTimeAndDateUtils;

    public FileUtils() {
        mTimeAndDateUtils = new TimeAndDateUtils();
    }

    public Map<String, List<GpsEntity>> readFileToSend() {
        Map<String, List<GpsEntity>> gpsEntityMap = new HashMap<>();
        //TODO implement reading
        //TODO key = file name to delete later
        return gpsEntityMap;
    }

    public void writeErrorToFile(String time, String errorText, String details) {
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
        File file = new File(Environment.getExternalStorageDirectory() + "/" + MAIN_FOLDER_NAME + "/" + fileName);
        if (file.exists()) {
            file.delete();
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
    }

    public void writeGpsToFile(GpsEntity gpsEntity) {
        PrintWriter out = null;
        try {
            File path = new File(Environment.getExternalStorageDirectory() + "/"
                    + MAIN_FOLDER_NAME, COORDINATES_FOLDER);
            if (!path.exists()) {
                path.mkdirs();
            }
            //TODO change file name every send interval
            File file = new File(path, getFileName());
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

    private String getFileName() {
        return mTimeAndDateUtils.getCurrentTimeForFileName() + ".txt";
    }

}