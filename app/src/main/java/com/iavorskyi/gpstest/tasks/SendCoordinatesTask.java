package com.iavorskyi.gpstest.tasks;

import android.os.AsyncTask;

import com.iavorskyi.gpstest.entities.GpsEntity;
import com.iavorskyi.gpstest.factory.RetrofitGenerator;
import com.iavorskyi.gpstest.rest.GpsApi;
import com.iavorskyi.gpstest.rest.json.BaseResponse;
import com.iavorskyi.gpstest.rest.json.SaveCoordinatesRequest;
import com.iavorskyi.gpstest.utils.FileUtils;
import com.iavorskyi.gpstest.utils.TimeAndDateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Response;

public class SendCoordinatesTask extends AsyncTask<GpsEntity, Void, Boolean> {

    private FileUtils mFileUtils = new FileUtils();

    protected Boolean doInBackground(GpsEntity... entities) {
        return sendDateToServer(mFileUtils.readFileToSend());
    }

    private boolean sendDateToServer(Map<String, List<GpsEntity>> entities) {
        if (entities != null && entities.size() > 0) {
            Set<String> keySet = entities.keySet();
            for (String fileName : keySet) {
                if (entities.size() > 0) {
                    List<SaveCoordinatesRequest> saveCoordinatesRequestList = new ArrayList<>();
                    for (GpsEntity gpsEntity : entities.get(fileName)) {
                        SaveCoordinatesRequest saveCoordinatesRequest = new SaveCoordinatesRequest(gpsEntity);
                        saveCoordinatesRequestList.add(saveCoordinatesRequest);
                    }
                    if (saveCoordinatesRequestList.size() > 0) {
                        GpsApi gpsApi = RetrofitGenerator.getRetrofit();
                        Call<BaseResponse> sendGeoParametersCall = gpsApi.sendGeoParameters(saveCoordinatesRequestList);
                        try {
                            Response<BaseResponse> response = sendGeoParametersCall.execute();
                            if (response != null && response.isSuccessful() && response.body().isSuccess()) {
                                mFileUtils.deleteSandedData(fileName);
                                mFileUtils.writeThatDataWasSanded(fileName);
                                return true;
                            } else {
                                if (response != null) {
                                    new FileUtils().writeErrorToFile(new TimeAndDateUtils().getDateAsStringFromSystemTime(
                                            System.currentTimeMillis()), "sending coordinates failed", response.errorBody().toString());
                                }
                            }
                        } catch (IOException e) {
                            new FileUtils().writeErrorToFile(new TimeAndDateUtils().getDateAsStringFromSystemTime(
                                    System.currentTimeMillis()), "sending coordinates failed with exception", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return false;
    }

}
