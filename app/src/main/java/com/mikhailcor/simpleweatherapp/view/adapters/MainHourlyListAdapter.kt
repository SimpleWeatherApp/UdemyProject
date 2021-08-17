package com.mikhailcor.simpleweatherapp.view.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.textview.MaterialTextView
import com.mikhailcor.simpleweatherapp.R
import com.mikhailcor.simpleweatherapp.business.model.HourlyWeatherModel
import com.mikhailcor.simpleweatherapp.view.*
import java.lang.StringBuilder

class MainHourlyListAdapter : BaseAdapter<HourlyWeatherModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main_hourly,parent,false)
        return HourlyViewHolder(view)
    }

    @SuppressLint("NonConstantResourceId")
    inner class HourlyViewHolder(view: View) : BaseViewHolder(view){

        @BindView(R.id.item_hourly_time_tv)
        lateinit var time: MaterialTextView

        @BindView(R.id.item_hourly_temp_tv)
        lateinit var temperature: MaterialTextView

        @BindView(R.id.item_hourly_pop_tv)
        lateinit var popRate: MaterialTextView

        @BindView(R.id.item_hourly_weather_condition_icon)
        lateinit var icon: ImageView

        init {
            ButterKnife.bind(this,itemView)
        }

        override fun bindView(position: Int) {

            mData[position].apply {
                time.text = dt.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
                temperature.text = StringBuilder().append(temp.toDegree()).append("°").toString()
                popRate.text = pop.toPercentString(" %")
                icon.setImageResource(weather[0].icon.provideIcon())
            }

        }

    }

}