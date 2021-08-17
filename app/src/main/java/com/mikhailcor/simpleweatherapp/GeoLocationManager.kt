package com.mikhailcor.simpleweatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import io.reactivex.rxjava3.subjects.BehaviorSubject
const val TAG1 = "GeoLocationManager"
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GeoLocationManager(private val context: Context) {

    val emitter : BehaviorSubject<Location> = BehaviorSubject.create()

    private val state = State()


    private val geoService = LocationServices.getFusedLocationProviderClient(context)
    private val tokenSource: CancellationTokenSource = CancellationTokenSource()
    private val locationRequest = LocationRequest.create().apply {
            interval = 600000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    }
    private val geoCallback = object : LocationCallback() {
        override fun onLocationResult(geo: LocationResult) {
            state.mLocation = geo.locations[0]
        }
    }

    fun updateGeo() {

    }
    fun closeAll() {

    }

    private fun checkGeoAvailability() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(context as MainActivity)
        val task = client.checkLocationSettings(builder.build())
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(context, 100)
                } catch (sendEx: IntentSender.SendIntentException) {

                }
            }
        }
        task.addOnSuccessListener { settingsResponse ->
            val localSets = settingsResponse.locationSettingsStates
            state.isAvailable =
                localSets?.run { isGpsPresent && isBleUsable && isLocationPresent && isLocationUsable }
                    ?: false
        }

    }

    @SuppressLint("MissingPermission")
    private fun requestGeo() {
        Log.d(TAG1, "requestGeo: ")
        geoService
                    .getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, tokenSource.token)
                    .addOnSuccessListener {
                        state.mLocation = it
                        Log.d(TAG1, "requestGeo: $it")
                    }
    }

    @SuppressLint("MissingPermission")
    private fun requestUpdateListener(){
        Log.d(TAG1, "requestUpdateListener: ")
        geoService.requestLocationUpdates(locationRequest,geoCallback,null)
    }


    inner class State {

        private val instructions = mutableListOf<() -> Unit>()
        private var index = 0;

        init {
            instructions.add { checkGeoAvailability() }
            instructions.add { requestGeo() }
            instructions.add { requestUpdateListener() }
            nextTask()
        }

        var isAvailable = false
            set(value) {
                field = value
                nextTask()
            }
        var mLocation: Location = Location("")
            set(value) {
                if (!value.isNotNull()) {
                    nextTask()
                } else {
                    field = value
                    notifyStateChanged()
                }
            }

        private fun notifyStateChanged() {
            emitter.onNext(mLocation)
        }

        private fun nextTask() {
            instructions[index++]()
        }

        private fun Location?.isNotNull() =
            !(this == null || this.longitude == 0.0 && this.longitude == 0.0)

    }
}