package com.mikhailcor.simpleweatherapp.business

import com.mikhailcor.simpleweatherapp.business.model.GeoCodeModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApi {

    @GET("geo/1.0/direct")
    fun getCityByName(
        @Query("q") name: String,
        @Query("limit") limit: String = "10",
        @Query("appid") appid: String = "ca8d1939be2fc1f8cc73a2e515d9ad1f",
    ) : Observable<List<GeoCodeModel>>

    @GET("geo/1.0/reverse?")
    fun getCityByCord(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("limit") limit: String = "10",
        @Query("appid") id: String = "ca8d1939be2fc1f8cc73a2e515d9ad1f"
    ) : Observable<List<GeoCodeModel>>
}