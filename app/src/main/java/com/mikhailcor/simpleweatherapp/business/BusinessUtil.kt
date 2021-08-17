package com.mikhailcor.simpleweatherapp.business

import com.mikhailcor.simpleweatherapp.business.model.GeoCodeModel
import com.mikhailcor.simpleweatherapp.business.room.GeoCodeEntity

fun GeoCodeModel.mapToEntity() = GeoCodeEntity(
    this.name,
    this.local_names,
    this.lat,
    this.lon,
    this.country,
    this.state ?: "",
    this.isFavorite
)

fun GeoCodeEntity.mapToModel() = GeoCodeModel(
    this.name,
    local_names,
    this.lat,
    this.lon,
    this.country,
    this.state,
    this.isFavorite
)