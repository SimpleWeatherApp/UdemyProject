package com.mikhailcor.simpleweatherapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikhailcor.simpleweatherapp.business.model.GeoCodeModel
import com.mikhailcor.simpleweatherapp.presenters.MenuPresenter
import com.mikhailcor.simpleweatherapp.view.MenuView
import com.mikhailcor.simpleweatherapp.view.adapters.CityListAdapter
import com.mikhailcor.simpleweatherapp.view.createObservable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_menu.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.util.concurrent.TimeUnit

class MenuActivity : MvpAppCompatActivity(), MenuView {

    private val presenter by moxyPresenter { MenuPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        presenter.enable()
        presenter.getFavoriteList()

        initCitiList(predictions)
        initCitiList(favorites)

        search_field.createObservable()
            .doOnNext { setLoading(true) }
            .debounce (700, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                if (!it.isNullOrEmpty())  presenter.searchFor(it)
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_left)
    }

    //-------------------------------- ^ menuActivity ^ -------------------------------

    //--------------------------------moxy-------------------------------
    override fun setLoading(flag: Boolean) {
        loading.isActivated = flag
        loading.visibility = if(flag) View.VISIBLE else View.GONE
    }

    override fun fillPredictionList(data: List<GeoCodeModel>) {
        (predictions.adapter as CityListAdapter).updateData(data)
    }

    override fun fillFavoriteList(data: List<GeoCodeModel>) {
        (favorites.adapter as CityListAdapter).updateData(data)
    }

    //--------------------------------moxy-------------------------------

    private fun initCitiList(rv: RecyclerView){
        val cityAdapter = CityListAdapter()
        cityAdapter.clickListener = searchItemClickListener
        rv.apply {
            adapter = cityAdapter
            layoutManager = object : LinearLayoutManager(this@MenuActivity, VERTICAL,false){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            setHasFixedSize(true)
        }

    }

    private val searchItemClickListener = object : CityListAdapter.SearchItemClickListener {
        override fun removeFromFavorite(item: GeoCodeModel) {
            presenter.removeLocation(item)
        }

        override fun addToFavorite(item: GeoCodeModel) {
            presenter.saveLocation(item)
        }

        override fun showWeatherIn(item: GeoCodeModel) {
            val intent = Intent(this@MenuActivity, MainActivity::class.java)
            val bundle = Bundle()
            bundle.putString("lat",item.lat.toString())
            bundle.putString("lon",item.lon.toString())
            intent.putExtra(COORDINATES,bundle)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_left)
        }

    }



}