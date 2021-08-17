package com.mikhailcor.simpleweatherapp

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

abstract class DailyBaseFragment<T> : Fragment()  {

    protected var mData : T? = null
    protected lateinit var fm: FragmentManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fm = (context as FragmentActivity).supportFragmentManager
    }



    fun setData(data: T){
        mData = data
        if(isVisible){
            updateView()
        }
    }

    protected abstract fun updateView()
}
