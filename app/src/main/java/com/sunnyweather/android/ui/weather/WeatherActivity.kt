package com.sunnyweather.android.ui.weather

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sunnyweather.android.R
import com.sunnyweather.android.databinding.ActivityWeatherBinding
import com.sunnyweather.android.databinding.ForecastBinding
import com.sunnyweather.android.databinding.LifeIndexBinding
import com.sunnyweather.android.databinding.NowBinding
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import com.sunnyweather.android.utils.showToast
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProviders.of(this).get(WeatherViewModel::class.java) }

    lateinit var binding:ActivityWeatherBinding

   /*
   private val viewModel by lazy { ViewModelProviders.of(this).get(WeatherViewModel::class.java) }

    private lateinit var binding:ActivityWeatherBinding
   private lateinit var nowBinding: NowBinding
    private lateinit var forecastBinding: ForecastBinding
    private lateinit var lifeIndexBinding: LifeIndexBinding*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_weather)
        val decorView=window.decorView
        decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //window.setDecorFitsSystemWindows(false)
        window.statusBarColor= Color.TRANSPARENT
        binding= ActivityWeatherBinding.inflate(layoutInflater)
        /*
        nowBinding= NowBinding.inflate(layoutInflater)
        forecastBinding= ForecastBinding.inflate(layoutInflater)
        lifeIndexBinding= LifeIndexBinding.inflate(layoutInflater)*/
        setContentView(binding.root)
        if (viewModel.locationLng.isEmpty()){
            viewModel.locationLng=intent.getStringExtra("location_lng")?:""
            Log.d("WeatherActivity","lng is ${viewModel.locationLng}")
        }
        if (viewModel.locationLat.isEmpty()){
            viewModel.locationLat=intent.getStringExtra("location_lat")?:""
            Log.d("WeatherActivity","lat is ${viewModel.locationLat}")
        }
        if (viewModel.placeName.isEmpty()){
            viewModel.placeName=intent.getStringExtra("place_name")?:""
            Log.d("WeatherActivity","palce is ${viewModel.placeName}")
        }
        viewModel.weatherLiveData.observe(this, Observer { result->
            val weather=result.getOrNull()
            if (weather!=null){
                showWeatherInfo(weather)
            }else{
                "无法成功获取天气信息".showToast()
                result.exceptionOrNull()?.printStackTrace()
            }
            binding.swiperefresh.isRefreshing=false
        })
        //binding.swiperefresh.setColorSchemeResources(R.color.design_default_color_primary)
        refreshWeather()
       // viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
        binding.swiperefresh.setOnRefreshListener {
            refreshWeather()
        }

        binding.nowXml.navBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object :DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerClosed(drawerView: View) {
                val manager=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
            }
        })
    }

    fun refreshWeather(){
        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
        binding.swiperefresh.isRefreshing=true
    }

    private fun showWeatherInfo(weather: Weather){
        Log.d("weather",weather.toString())
        val realtime=weather.realtime
        val daily=weather.daily
        //填充now.xml布局中的数据
       /* nowBinding.placeName.text=viewModel.placeName
        val currentTempText="${realtime.temperature.toInt()}℃"
        nowBinding.currentTemp.text=currentTempText
        nowBinding.currentSky.text= getSky(realtime.skycon).info
        val currentPM25Text="空气指数${realtime.airQuality.aqi.chn.toInt()}"
        nowBinding.currentAQI.text=currentPM25Text
        nowBinding.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)*/
        binding.nowXml.placeName.text=viewModel.placeName
        val currentTempText="${realtime.temperature.toInt()}℃"
        binding.nowXml.currentTemp.text=currentTempText
        binding.nowXml.currentSky.text= getSky(realtime.skycon).info
        val currentPM25Text="空气指数${realtime.airQuality.aqi.chn.toInt()}"
        binding.nowXml.currentAQI.text=currentPM25Text
        binding.nowXml.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        //填充forecast.xml布局中的数据
        /*forecastBinding.forecastLayout.removeAllViews()
        val days=daily.skycon.size
        for (i in 0 until days){
            val skycon=daily.skycon[i]
            val temperature=daily.temperature[i]
            val view=LayoutInflater.from(this).inflate(R.layout.forecast_item,
                forecastBinding.forecastLayout,false)
            val dateInfo=view.findViewById<TextView>(R.id.dateInfo)
            val skyIcon=view.findViewById<ImageView>(R.id.skyIcon)
            val skyInfo=view.findViewById<TextView>(R.id.skyInfo)
            val temperatureInfo=view.findViewById<TextView>(R.id.temperatureInfo)
            val simpleDateFormat=SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            dateInfo.text=simpleDateFormat.format(skycon.date)
            val sky= getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text=sky.info
            val tempText="${temperature.min.toInt()}~${temperature.max.toInt()}℃"
            temperatureInfo.text=tempText
            forecastBinding.forecastLayout.addView(view)
        }*/
        binding.forecastXml.forecastLayout.removeAllViews()
        val days=daily.skycon.size
        for (i in 0 until days){
            val skycon=daily.skycon[i]
            val temperature=daily.temperature[i]
            val view=LayoutInflater.from(this).inflate(R.layout.forecast_item,
                binding.forecastXml.forecastLayout,false)
            val dateInfo=view.findViewById<TextView>(R.id.dateInfo)
            val skyIcon=view.findViewById<ImageView>(R.id.skyIcon)
            val skyInfo=view.findViewById<TextView>(R.id.skyInfo)
            val temperatureInfo=view.findViewById<TextView>(R.id.temperatureInfo)
            val simpleDateFormat=SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            dateInfo.text=simpleDateFormat.format(skycon.date)
            val sky= getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text=sky.info
            val tempText="${temperature.min.toInt()}~${temperature.max.toInt()}℃"
            temperatureInfo.text=tempText
            binding.forecastXml.forecastLayout.addView(view)
        }
        //填充life_index.xml布局中的数据
       /* val lifeIndex=daily.lifeIndex
        lifeIndexBinding.coldRiskText.text=lifeIndex.coldRisk[0].desc
        lifeIndexBinding.dressingText.text=lifeIndex.dressing[0].desc
        lifeIndexBinding.ultravioletText.text=lifeIndex.ultraviolet[0].desc
        lifeIndexBinding.carWashingText.text=lifeIndex.carWashing[0].desc
        binding.weatherLayout.visibility= View.VISIBLE*/
        val lifeIndex=daily.lifeIndex
        binding.lifeIndexXml.coldRiskText.text=lifeIndex.coldRisk[0].desc
        binding.lifeIndexXml.dressingText.text=lifeIndex.dressing[0].desc
        binding.lifeIndexXml.ultravioletText.text=lifeIndex.ultraviolet[0].desc
        binding.lifeIndexXml.carWashingText.text=lifeIndex.carWashing[0].desc
        binding.weatherLayout.visibility= View.VISIBLE
    }
}