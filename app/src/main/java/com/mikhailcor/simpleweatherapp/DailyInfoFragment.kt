package com.mikhailcor.simpleweatherapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.TransitionInflater
import com.mikhailcor.simpleweatherapp.business.model.DailyWeatherModel
import com.mikhailcor.simpleweatherapp.view.*
import kotlinx.android.synthetic.main.fragment_day_info.*

class DailyInfoFragment : DailyBaseFragment<DailyWeatherModel>() {

    private lateinit var viewContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_right)
        enterTransition = inflater.inflateTransition(R.transition.slide_right)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_day_info,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_back.setOnClickListener {
            fm.popBackStack()
        }
        viewContext = view.context
        updateView()
    }



    override fun updateView() {

        mData?.apply {
            day_date.text = dt.toDateFormatOf(DAY_FULL_MONTH_NAME)
            day_temp.text = viewContext.getString(R.string.degree_symbol, temp.getAverage().toDegree())
            day_icon.setImageResource(weather[0].icon.provideIcon())
            day_morn_temp.text = viewContext.getString(R.string.degree_symbol, temp.morn.toDegree())
            day_morn_fl.text = viewContext.getString(R.string.degree_symbol, feels_like.morn.toDegree())
            day_daily_temp.text = viewContext.getString(R.string.degree_symbol, temp.day.toDegree())
            day_daily_fl.text = viewContext.getString(R.string.degree_symbol, feels_like.day.toDegree())
            day_eve_temp.text = viewContext.getString(R.string.degree_symbol, temp.eve.toDegree())
            day_eve_fl.text = viewContext.getString(R.string.degree_symbol, feels_like.eve.toDegree())
            day_night_fl.text = viewContext.getString(R.string.degree_symbol, feels_like.night.toDegree())
            day_night_temp.text = viewContext.getString(R.string.degree_symbol, temp.night.toDegree())
            day_humidity.text = ("$humidity %")
            val settingPressure = SettingsHolder.pressure
            day_pressure.text = viewContext.getString(settingPressure.mesureUnitStringRes,settingPressure.getValue(pressure.toDouble()))
            val settingWindSpeed = SettingsHolder.windSpeed
            day_wind_speed.text = viewContext.getString(settingWindSpeed.mesureUnitStringRes,settingWindSpeed.getValue(wind_speed))
            day_wind_dir.text = wind_deg.toString()
            day_sunrise.text = sunrise.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
            day_sunset.text = sunset.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
        }

    }
}