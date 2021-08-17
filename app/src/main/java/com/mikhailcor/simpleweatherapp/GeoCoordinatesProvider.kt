package com.mikhailcor.simpleweatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import io.reactivex.rxjava3.subjects.BehaviorSubject

const val TAGS = "GEO_PROVIDER"

@SuppressLint("MissingPermission")
class GeoCoordinatesProvider(private val context: Context) {

    val mLocation: BehaviorSubject<Location> = BehaviorSubject.create()

    private var locationListenerIsActive = false
    private val tokenSource: CancellationTokenSource = CancellationTokenSource()
    private val geoService by lazy { LocationServices.getFusedLocationProviderClient(context) }

    private val locationRequest by lazy {
        LocationRequest.create().apply {
            interval = 600000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
    private val geoCallback = object : LocationCallback() {
        override fun onLocationResult(geo: LocationResult) {
            Log.d(TAG1, "geoCallBack: ${geo.locations[0]}")
            mLocation.onNext(geo.locations[0])
        }
    }

    fun enable() {
        val task =
            geoService.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, tokenSource.token)
        task.addOnSuccessListener {
            Log.d(TAG1, "enable: $it")
            if (it.isNotNull()) mLocation.onNext(it)
            else {
                Log.d(TAG1, "enable: else branch ")
                locationListenerIsActive = true
                geoService.requestLocationUpdates(locationRequest, geoCallback, null)
            }
        }
        task.addOnFailureListener { Log.d(TAG1, "enable: Have an issue") }
    }

    fun diable() {
        tokenSource.cancel()
        dismissLocationRequest()
    }

    private fun dismissLocationRequest() {
        if (locationListenerIsActive) {
            geoService.removeLocationUpdates(geoCallback)
            locationListenerIsActive = false
        }
    }


    private fun Location?.isNotNull() =
        !(this == null || this.longitude == 0.0 && this.longitude == 0.0)

    override fun toString(): String {
        return "BehaciorSubject: ${mLocation.value}, " +
                "locationListenerIsActive: $locationListenerIsActive, "
    }
}
