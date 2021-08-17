package com.mikhailcor.simpleweatherapp.presenters

import android.util.Log
import com.mikhailcor.simpleweatherapp.business.ApiProvider
import com.mikhailcor.simpleweatherapp.business.repos.MainRepository
import com.mikhailcor.simpleweatherapp.view.MainView

class MainPresenter : BasePresenter<MainView>() {
    private val repo = MainRepository(ApiProvider())

    override fun enable() {
        repo.dataEmitter
            .doAfterNext { viewState.setLoading(false) }
            .subscribe { response ->
                Log.d("MAINREPO", " Presenter enable(): $response")
                viewState.displayLocation(response.cityName)
                viewState.displayCurrentData(response.weatherData)
                viewState.displayDailyData(response.weatherData.daily)
                viewState.displayHourlyData(response.weatherData.hourly)
                response.error?.let { viewState.displayError(response.error) }
            }
    }

    fun refresh(lat: String, lon: String) {
        viewState.setLoading(true)
        repo.reloadData(lat, lon)
    }

}