package com.sunnyweather.android.ui.weather

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Location

class WeatherViewModel:ViewModel() {

    private val locationLiveData=MutableLiveData<Location>()

    var locationLng=""
    var locationLat=""
    var placeName=""

    val weatherLiveData=Transformations.switchMap(locationLiveData){
        Log.d("weatherLiveData","lng is ${it.lng},lat is ${it.lat}")
        Repository.refreshWeather(it.lng,it.lat)
    }

    fun refreshWeather(lng:String,lat:String){
        Log.d("WeatherViewModel","lng is ${lng},lat is ${lat}")
        locationLiveData.value= Location(lng,lat)
    }

}