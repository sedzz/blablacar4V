package org.cuatrovientos.blabla4v.interfaces;

import org.cuatrovientos.blabla4v.models.RouteResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/v2/directions/driving-car")
    Call<RouteResponse> getRoute(@Query("api_key") String key, @Query("start") String end, @Query("end") String start);}