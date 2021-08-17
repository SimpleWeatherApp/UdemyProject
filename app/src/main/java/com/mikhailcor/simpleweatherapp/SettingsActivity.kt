package com.mikhailcor.simpleweatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButtonToggleGroup
import com.mikhailcor.simpleweatherapp.view.SettingsHolder
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        inner_toolbar.setNavigationOnClickListener { onBackPressed() }

        setSavedSettings()

        listOf(groupTemp, groupWindSpeed, groupPressure).forEach {
            it.addOnButtonCheckedListener(
                ToggleButtonClickListener
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SettingsHolder.onDestroy()
    }

    private fun setSavedSettings() {
        groupWindSpeed.check(SettingsHolder.windSpeed.checkedViewId)
        groupTemp.check(SettingsHolder.temp.checkedViewId)
        groupPressure.check(SettingsHolder.pressure.checkedViewId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right)
    }

    private object ToggleButtonClickListener : MaterialButtonToggleGroup.OnButtonCheckedListener {
        override fun onButtonChecked(
            group: MaterialButtonToggleGroup?,
            checkedId: Int,
            isChecked: Boolean
        ) {

            when (checkedId to isChecked) {
                R.id.degreeC to true -> SettingsHolder.temp = SettingsHolder.Setting.TEMP_CELSIUS
                R.id.degreeF to true -> SettingsHolder.temp = SettingsHolder.Setting.TEMP_FAHRENHEIT
                R.id.speed_ms to true -> SettingsHolder.windSpeed = SettingsHolder.Setting.WIND_SPEED_MS
                R.id.speed_kmh to true -> SettingsHolder.windSpeed = SettingsHolder.Setting.WIND_SPEED_KMH
                R.id.pressure_mmHg to true -> SettingsHolder.pressure = SettingsHolder.Setting.PRESSURE_MMHG
                R.id.pressure_hPa to true -> SettingsHolder.pressure = SettingsHolder.Setting.PRESSURE_HPA
            }

        }


    }
}