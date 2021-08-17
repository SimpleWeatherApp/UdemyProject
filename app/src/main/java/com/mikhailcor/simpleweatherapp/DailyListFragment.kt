package com.mikhailcor.simpleweatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.mikhailcor.simpleweatherapp.business.model.DailyWeatherModel
import com.mikhailcor.simpleweatherapp.view.adapters.MainDailyListAdapter
import kotlinx.android.synthetic.main.frgment_daily_list.*

class DailyListFragment : DailyBaseFragment<List<DailyWeatherModel>>() {

    private val dailyAdapter = MainDailyListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_out_right)
        enterTransition = inflater.inflateTransition(R.transition.slide_out_right)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frgment_daily_list,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dailyList.apply {
            adapter = dailyAdapter
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL,false )
            setHasFixedSize(true)
            dailyAdapter.clickListener = clickListener
        }

        mData?.let { updateView() }
    }

    override fun updateView() {
        dailyAdapter.updateData(mData!!)
    }

    private val clickListener = object : MainDailyListAdapter.DayItemClick{
        override fun showDetails(data: DailyWeatherModel) {
            val fragment = DailyInfoFragment()
            fragment.setData(data)
            fm.beginTransaction().replace(R.id.fragmentContainer,fragment).addToBackStack(null).commit()

        }

    }

}