package com.mikhailcor.simpleweatherapp.business.repos

import com.mikhailcor.simpleweatherapp.App
import com.mikhailcor.simpleweatherapp.business.ApiProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*

abstract class BaseRepository<T>(val api: ApiProvider) {

    val dataEmitter: BehaviorSubject<T> = BehaviorSubject.create()
    protected val db by lazy { App.db }

    protected fun roomTransaction(
        transaction: () -> T,
    ) = Observable.fromCallable { transaction() }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe{dataEmitter.onNext(it)}

}