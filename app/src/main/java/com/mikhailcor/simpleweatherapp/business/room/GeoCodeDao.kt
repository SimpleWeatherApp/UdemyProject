package com.mikhailcor.simpleweatherapp.business.room

import androidx.room.*

@Dao
interface GeoCodeDao {

    @Query("SELECT * FROM GeoCode")
    fun getAll() : List<GeoCodeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(item: GeoCodeEntity)

    @Delete
    fun remove(item: GeoCodeEntity)
}