package com.sunnyweather.android.utils

import android.widget.Toast
import com.sunnyweather.android.SunnyWeatherApplication

fun String.showToast(duration: Int=Toast.LENGTH_SHORT){
    Toast.makeText(SunnyWeatherApplication.context,this,duration).show()
}

fun Int.showToast(duration: Int=Toast.LENGTH_SHORT){
    Toast.makeText(SunnyWeatherApplication.context,this,duration).show()
}

