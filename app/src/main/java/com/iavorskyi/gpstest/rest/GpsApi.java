package com.iavorskyi.gpstest.rest;

import com.iavorskyi.gpstest.rest.json.BaseResponse;
import com.iavorskyi.gpstest.rest.json.SaveCoordinatesRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GpsApi {

    @POST("mobileGeoParameters/SaveGeoParameters")
    Call<BaseResponse> sendGeoParameters(@Body List<SaveCoordinatesRequest> geoParameters);

}
