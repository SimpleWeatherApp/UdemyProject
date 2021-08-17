package com.mikhailcor.simpleweatherapp.business.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.mikhailcor.simpleweatherapp.business.model.LocalNames

@Entity(tableName = "GeoCode", primaryKeys = ["lat","lon"])
class GeoCodeEntity (
    @ColumnInfo(name = "country") val country: String,
    @Embedded val local_names: LocalNames,
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "lon") val lon: Double,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "state") val state: String,
    @ColumnInfo(name = "isFavorite") var isFavorite: Boolean = false
)