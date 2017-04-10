package com.iavorskyi.gpstest.factory;

import com.iavorskyi.gpstest.rest.GpsApi;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

public class RetrofitGenerator {

    private static final String BASE_URL = "https://amt2.estafeta.org/api/";
    private static GpsApi mGpsApi;

    public static GpsApi getRetrofit() {
        if (mGpsApi == null)
            mGpsApi = createRetrofit();
        return mGpsApi;
    }

    private static GpsApi createRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(GpsApi.class);
    }

}
