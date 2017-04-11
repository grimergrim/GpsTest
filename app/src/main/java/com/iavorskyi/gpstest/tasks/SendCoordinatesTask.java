package com.iavorskyi.gpstest.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.iavorskyi.gpstest.entities.GpsEntity;
import com.iavorskyi.gpstest.factory.RetrofitGenerator;
import com.iavorskyi.gpstest.rest.HttpApi;
import com.iavorskyi.gpstest.rest.json.BaseResponse;
import com.iavorskyi.gpstest.rest.json.SendCoordinatesRequest;
import com.iavorskyi.gpstest.services.SendingFinishedListener;
import com.iavorskyi.gpstest.utils.FileUtils;
import com.iavorskyi.gpstest.utils.TimeAndDateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Response;

public class SendCoordinatesTask extends AsyncTask<Void, Void, Boolean> {

    private FileUtils mFileUtils = new FileUtils();
    private SendingFinishedListener mSendingFinishedListener;
    private Context mContext;
    private HttpApi mHttpApi;

    protected Boolean doInBackground(Void... params) {
        Map<String, List<GpsEntity>> map = mFileUtils.readFileToSend();
        sendDateToServer(map);
        return true;
    }

    protected void onPostExecute(Boolean result) {
        Log.e("=============", "task on postExecute: ");
        if (mSendingFinishedListener != null)
            mSendingFinishedListener.sendingFinished();
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setListener(SendingFinishedListener listener) {
        this.mSendingFinishedListener = listener;
    }

    private void sendDateToServer(Map<String, List<GpsEntity>> entities) {
        mHttpApi = RetrofitGenerator.getRetrofit(mContext);
        Log.e("=============", "task doInBackground");
        if (entities != null && entities.size() > 0) {
            Set<String> keySet = entities.keySet();
            for (String fileName : keySet) {
                if (entities.size() > 0) {
                    List<SendCoordinatesRequest> sendCoordinatesRequestList = new ArrayList<>();
                    for (GpsEntity gpsEntity : entities.get(fileName)) {
                        SendCoordinatesRequest sendCoordinatesRequest = new SendCoordinatesRequest(gpsEntity);
                        sendCoordinatesRequestList.add(sendCoordinatesRequest);
                    }
                    if (sendCoordinatesRequestList.size() > 0) {
                        makeCall(fileName, sendCoordinatesRequestList);
                    }
                }
            }
        }
    }

    private boolean makeCall(String fileName, List<SendCoordinatesRequest> sendCoordinatesRequestList) {
        if (mHttpApi != null) {
            Call<BaseResponse> sendGeoParametersCall = mHttpApi.sendGeoParameters(sendCoordinatesRequestList);
            try {
                Response<BaseResponse> response = sendGeoParametersCall.execute();
                if (response != null && response.isSuccessful() && response.body().isSuccess()) {
                    Log.e("=============", "Response: " + response.body());
                    Log.e("=============", "Response success is: " + response.body().isSuccess());
                    Log.e("=============", "data was sended: " + fileName);
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
        } else {
            //TODO report error
        }
        return false;
    }

}
