package com.mikhailcor.simpleweatherapp.business

import com.mikhailcor.simpleweatherapp.business.model.WeatherDataModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/onecall?")
    fun getWeatherForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = "minutely,alerts",
        @Query("appid") apiid: String = "ca8d1939be2fc1f8cc73a2e515d9ad1f",
        @Query("lang") lang: String = "en"
    ) : Observable<WeatherDataModel>
}