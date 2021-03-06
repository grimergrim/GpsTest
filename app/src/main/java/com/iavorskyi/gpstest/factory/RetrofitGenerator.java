package com.iavorskyi.gpstest.factory;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iavorskyi.gpstest.Constants;
import com.iavorskyi.gpstest.rest.HttpApi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitGenerator {

    private static final String BASE_URL = "https://amt2.estafeta.org/api/";
    private static final String REQUEST_AUTHORIZATION = "Authorization";
    private static final String REQUEST_CLIENT_VERSION = "ClientVersion";
    private static final String GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String REQUEST_ACCEPT = "Accept";
    private static final String REQUEST_ACCEPT_VALUE = "application/json";
    private static final String APP_VERSION = "4.25.0";
    private static HttpApi sMHttpApi;

    public static HttpApi getRetrofit(Context context) {
        if (sMHttpApi == null && context != null) {
            sMHttpApi = createRetrofit(getAuthHeader(getToken(context)), APP_VERSION);
        }
        return sMHttpApi;
    }

    public static HttpApi getRetrofitForLogin() {
        Gson gson = new GsonBuilder().setDateFormat(GSON_DATE_FORMAT).create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(gsonConverterFactory);
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(HttpApi.class);
    }

    private static HttpApi createRetrofit(final String token, final String version) {
        Gson gson = new GsonBuilder().setDateFormat(GSON_DATE_FORMAT).create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();

        long SERVICE_TIMEOUT = 120L; //seconds
        OkHttpClient okHttpClient = okHttpBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header(REQUEST_AUTHORIZATION, token)
                        .header(REQUEST_ACCEPT, REQUEST_ACCEPT_VALUE)
                        .header(REQUEST_CLIENT_VERSION, version)
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        }).readTimeout(SERVICE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(SERVICE_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofitBuilder.baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory);

        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(HttpApi.class);
    }

    private static String getToken(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getString("NewGpsTrackerToken", "");
    }

    private static String getAuthHeader(String token) {
        String authHeader = token + "@" + Constants.COMPANY_ID;
        authHeader = authHeader.replace("\n", "").replace("\r", "").trim();
        byte[] data = new byte[0];
        try {
            data = authHeader.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String encodedString = Base64.encodeToString(data, Base64.DEFAULT);
        return ("token " + encodedString).replace("\n", "").replace("\r", "").trim();
    }

}
