package com.iavorskyi.gpstest.tasks;

import android.os.AsyncTask;

import com.iavorskyi.gpstest.entities.GpsEntity;
import com.iavorskyi.gpstest.utils.FileUtils;

import java.util.Map;
import java.util.Set;

public class SaveDataTask extends AsyncTask<Map<String, GpsEntity>, Void, Void> {

    @SafeVarargs
    @Override
    protected final Void doInBackground(Map<String, GpsEntity>... params) {
        if (params.length > 0 && params[0] != null) {
            Set<String> keySet = params[0].keySet();
            for (String key : keySet) {
                GpsEntity gpsEntity = params[0].get(key);
                new FileUtils().writeGpsToFile(gpsEntity, key);
            }
        }
        return null;
    }

}
