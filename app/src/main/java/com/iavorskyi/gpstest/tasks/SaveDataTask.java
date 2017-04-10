package com.iavorskyi.gpstest.tasks;

import android.os.AsyncTask;

import com.iavorskyi.gpstest.entities.GpsEntity;
import com.iavorskyi.gpstest.utils.FileUtils;

public class SaveDataTask extends AsyncTask<GpsEntity, Void, Void> {

    protected Void doInBackground(GpsEntity... entities) {
        if (entities.length > 0 && entities[0] != null)
        new FileUtils().writeGpsToFile(entities[0]);
        return null;
    }

}
