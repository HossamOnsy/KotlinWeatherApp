package com.hossam.android.weatherfreakingapp.utils

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
    val PREFS_FILENAME = "preferences.WeatherFreakingApp"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);
    companion object {
        const val TEMPRATURE = "Temperature"
        const val LOCATION = "Location"
        const val NOTIFICATION ="Notification"
    }
}