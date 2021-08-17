package com.mikhailcor.simpleweatherapp.business.repos

import com.mikhailcor.simpleweatherapp.business.ApiProvider
import com.mikhailcor.simpleweatherapp.business.mapToEntity
import com.mikhailcor.simpleweatherapp.business.mapToModel
import com.mikhailcor.simpleweatherapp.business.model.GeoCodeModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

const val SAVED = 1
const val CURRENT = 0
class MenuRepository(api: ApiProvider) : BaseRepository<MenuRepository.Content>(api) {

    private val dbAccess = db.getGeoCodeDao()

    fun getCities(name: String) {
        api.provideGeoCodeApi().getCityByName(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                Content(it, CURRENT)
            }
            .subscribe {
                dataEmitter.onNext(it)
            }
    }

    fun add(data: GeoCodeModel) {
        getFavoriteListWith { dbAccess.add(data.mapToEntity()) }
    }

    fun remove(data: GeoCodeModel) {
        getFavoriteListWith { dbAccess.remove(data.mapToEntity()) }
    }

    fun updateFavorite() {
        getFavoriteListWith()
    }


    private fun getFavoriteListWith(daoQuery: (() -> Unit)? = null) {
        roomTransaction {
            daoQuery?.let { it() }
            Content(dbAccess.getAll().map { it.mapToModel() }, SAVED)
        }
    }

    data class Content(val data: List<GeoCodeModel>, val purpose: Int)
}