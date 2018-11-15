package com.hossam.android.weatherfreakingapp.utils

import android.graphics.ColorSpace
import com.hossam.android.weatherfreakingapp.modelskotlin.WeatherList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServiceInterface {
    @GET("weather")
    fun getWeatherData():
            Observable<WeatherList>
}