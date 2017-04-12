package com.iavorskyi.gpstest.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.iavorskyi.gpstest.factory.RetrofitGenerator;
import com.iavorskyi.gpstest.rest.HttpApi;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class LoginTask extends AsyncTask<String, Void, Void> {

    private Context mContext;

    @Override
    protected Void doInBackground(String... params) {
        if (params.length > 1 && params[0] != null && params[1] != null) {
            String login = params[0];
            String password = params[1];
            login(login, password);
        }
        return null;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    private void login(String login, String password) {
        HttpApi httpApi = RetrofitGenerator.getRetrofitForLogin();
        Call<String> loginCall = httpApi.login(login, password);
        try {
            Response<String> response = loginCall.execute();
            if (response.isSuccessful() && response.body() != null && response.body().length() > 0) {
                String token = response.body();
                saveToken(token);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToken(String token) {
        if (mContext != null) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = defaultSharedPreferences.edit();
            editor.putString("NewGpsTrackerToken", token);
            editor.apply();
        }
    }

}
