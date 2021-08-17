package com.mikhailcor.simpleweatherapp.view

import com.mikhailcor.simpleweatherapp.business.model.DailyWeatherModel
import com.mikhailcor.simpleweatherapp.business.model.HourlyWeatherModel
import com.mikhailcor.simpleweatherapp.business.model.WeatherDataModel
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MainView : MvpView {

    @AddToEndSingle
    fun displayLocation(data: String)

    @AddToEndSingle
    fun displayCurrentData(data: WeatherDataModel)

    @AddToEndSingle
    fun displayHourlyData(data: List<HourlyWeatherModel>)

    @AddToEndSingle
    fun displayDailyData(data: List<DailyWeatherModel>)

    @AddToEndSingle
    fun displayError(error: Throwable)

    @AddToEndSingle
    fun setLoading(flag: Boolean)

}