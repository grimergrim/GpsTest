package com.iavorskyi.gpstest.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetUtils {

    private Context mContext;

    public InternetUtils(Context context) {
        mContext = context;
    }

    public boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
