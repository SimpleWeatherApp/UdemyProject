package com.mikhailcor.simpleweatherapp.business.model

data class Temp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
){
    fun getAverage() = (day+night+eve+morn) / 4
}