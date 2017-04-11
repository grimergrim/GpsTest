package com.iavorskyi.gpstest.rest;

import com.iavorskyi.gpstest.rest.json.BaseResponse;
import com.iavorskyi.gpstest.rest.json.SendCoordinatesRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface HttpApi {

    @POST("mobileGeoParameters/SaveGeoParameters")
    Call<BaseResponse> sendGeoParameters(@Body List<SendCoordinatesRequest> sendCoordinatesRequestList);

    @FormUrlEncoded
    @POST("mobileUsers/login")
    Call<String> login(@Field("Login") String login, @Field("Password") String password);

}
