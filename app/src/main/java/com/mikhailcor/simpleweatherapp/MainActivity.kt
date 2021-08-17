package com.mikhailcor.simpleweatherapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import com.mikhailcor.simpleweatherapp.business.model.DailyWeatherModel
import com.mikhailcor.simpleweatherapp.business.model.HourlyWeatherModel
import com.mikhailcor.simpleweatherapp.business.model.WeatherDataModel
import com.mikhailcor.simpleweatherapp.presenters.MainPresenter
import com.mikhailcor.simpleweatherapp.view.*
import com.mikhailcor.simpleweatherapp.view.adapters.MainHourlyListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.lang.Exception
import java.lang.StringBuilder
import kotlin.math.roundToInt

const val TAG = "GEO_TEST"
const val COORDINATES = "Coordinates"

class MainActivity : MvpAppCompatActivity(), MainView {

    private val mainPresenter by moxyPresenter { MainPresenter() }
    private val tokenSource: CancellationTokenSource = CancellationTokenSource()
    private val geoService by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationRequest by lazy {
        LocationRequest.create().apply {
            interval = 600000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
    private val geoCallback = object : LocationCallback() {
        override fun onLocationResult(geo: LocationResult) {
            Log.d(TAG, "onLocationResult: callback ${geo.locations[0]}")
            mLocation = geo.locations[0]
            mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
        }
    }

    private lateinit var mLocation: Location

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomSheets()
        initViews()
        initSwipeRefrash()



        refresh.isRefreshing = true

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, DailyListFragment(), DailyListFragment::class.simpleName)
            .commit()

        if (!intent.hasExtra(COORDINATES)) {
            checkGeoAvailability()
            Log.d(TAG, "onCreate: ")
            getGeo()
        } else {
            val coord = intent.extras!!.getBundle(COORDINATES)!!
            val loc = Location("")
            loc.latitude = coord.getString("lat")!!.toDouble()
            loc.longitude = coord.getString("lon")!!.toDouble()
            mLocation = loc
            mainPresenter.refresh(
                lat = mLocation.latitude.toString(),
                lon = mLocation.longitude.toString()
            )
        }

        main_settings_btn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out)
        }

        main_menu_btn.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, android.R.anim.fade_out)
        }

        main_hourly_list.apply {
            adapter = MainHourlyListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        mainPresenter.enable()

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")

    }

    private fun initViews() {
        main_city_name_tv.text = "Moscow"
        main_date_tv.text = "1 april"
        main_weather_condition_icon.setImageResource(R.drawable.ic_sun)
        main_temp.text = "25\u00B0"
        main_temp_min_tv.text = "19"
        main_temp_max_tv.text = "28"
        main_weather_image.setImageResource(R.mipmap.clowd3x)
        main_pressure_mu_tv.text = "1023 hPa"
        main_humidity_mu_tv.text = "88 %"
        main_wind_speed_mu_tv.text = "5 m/s"
        main_sunrise_mu_tv.text = "4:30"
        main_sunset_mu_tv.text = "22:43"
    }
    // -------------  moxy code -------------

    override fun displayLocation(data: String) {
        main_city_name_tv.text = data
    }

    override fun displayCurrentData(data: WeatherDataModel) {
        data.apply {
            main_date_tv.text = current.dt.toDateFormatOf(DAY_FULL_MONTH_NAME)
            main_weather_condition_icon.setImageResource(current.weather[0].icon.provideIcon())
            main_temp.text = StringBuilder().append(current.temp.toDegree()).append("°").toString()
            daily[0].temp.apply {
                main_temp_min_tv.text = min.toDegree()
                main_temp_max_tv.text = max.toDegree()
            }

            val pressureSet = SettingsHolder.pressure
            main_pressure_mu_tv.text = getString(
                pressureSet.mesureUnitStringRes,
                pressureSet.getValue(current.pressure.toDouble())
            )

            val windSpeedSet = SettingsHolder.windSpeed
            main_wind_speed_mu_tv.text = getString(
                windSpeedSet.mesureUnitStringRes,
                windSpeedSet.getValue(current.wind_speed)
            )

            main_weather_condition_description.text = current.weather[0].description
            main_weather_image.setImageResource(R.mipmap.clowd3x)
            main_humidity_mu_tv.text =
                StringBuilder().append(current.humidity.toString()).append(" %").toString()
            main_sunrise_mu_tv.text = current.sunrise.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
            main_sunset_mu_tv.text = current.sunset.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
        }
    }

    override fun displayHourlyData(data: List<HourlyWeatherModel>) {
        (main_hourly_list.adapter as MainHourlyListAdapter)
            .updateData(data)
    }

    override fun displayDailyData(data: List<DailyWeatherModel>) {
        (supportFragmentManager.findFragmentByTag(DailyListFragment::class.simpleName) as DailyListFragment)
            .setData(data)
    }

    override fun displayError(error: Throwable) {
        Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
    }

    override fun setLoading(flag: Boolean) {
        refresh.isRefreshing = flag
    }

    // -------------  moxy code -------------

    //-------------- geo -------------------
    @SuppressLint("MissingPermission")
    private fun getGeo(){
        geoService
            .getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, tokenSource.token)
            .addOnSuccessListener {
                Log.d(TAG, "getGeo: ")
                if(it!=null){
                    mLocation = it
                    mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
                }else{
                    displayError(Exception("Geodata is not available"))
                }
                Log.d(TAG, "requestGeo: $it")
            }
    }

    private fun checkGeoAvailability() {
        Log.d(TAG, "checkGeoAvailability: ")
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, 100)
                } catch (sendEx: IntentSender.SendIntentException) {

                }
            }
        }
    }
    //-------------- geo -------------------


    private fun initBottomSheets() {
        main_bottom_sheets.isNestedScrollingEnabled = true
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        main_bottom_sheets_container.layoutParams =
            CoordinatorLayout.LayoutParams(size.x, (size.y * 0.5).roundToInt())
    }

    private fun initSwipeRefrash() {
        refresh.apply {
            setColorSchemeResources(R.color.purple_700)
            setProgressViewEndTarget(false, 280)
            setOnRefreshListener {
                mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
            }

        }
    }
}