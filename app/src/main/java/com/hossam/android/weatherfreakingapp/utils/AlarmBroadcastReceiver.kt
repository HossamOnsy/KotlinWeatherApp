package com.hossam.android.weatherfreakingapp.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hossam.android.weatherfreakingapp.R
import com.hossam.android.weatherfreakingapp.R.id.*
import com.hossam.android.weatherfreakingapp.WeatherAdapter
import com.hossam.android.weatherfreakingapp.modelskotlin.ListItem
import com.hossam.android.weatherfreakingapp.views.DetailActivity
import com.hossam.android.weatherfreakingapp.views.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class AlarmBroadcastReceiver : BroadcastReceiver() {
    lateinit var shareBody :String
    override fun onReceive(context: Context?, intent: Intent?) {
            fetchData(context,intent)


    }
    private fun fetchData(context: Context?, intent: Intent?) {

        if (context != null) {
            WebServiceEngine.getRetrofit(context).getWeatherData()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {}
                    .doFinally { }
                    .doOnError { }
                    .subscribe { e ->
                        var list: MutableList<ListItem?>? = e.list as MutableList<ListItem?>?
                        var x =list!!.get(0)

                        val xx = Prefs(context!!)
                        var prefs = xx.prefs.getString(Prefs.TEMPRATURE, "")
                        var text_min_degree_text : String= ""
                        var text_max_degree_text : String = ""
                        if(prefs.equals(context.getString(R.string.imperial))){
                            text_min_degree_text = Math.round(convertCelciusToFahrenheit(x?.temp?.min!!)).toString()
                        }else if(prefs.equals(context.getString(R.string.metric))){
                            text_max_degree_text = Math.round(convertFahrenheitToCelcius(x?.temp?.min!!)).toString()
                        }

                        val cal = Calendar.getInstance()
                        val format1 = SimpleDateFormat("dd MMM YYYY")
                        var yourfinaldate: String = context.getString(R.string.today) + " " + format1.format(cal.getTime());
                        shareBody= yourfinaldate.toString()+" MaxDegree : " + text_max_degree_text+" MinDegree : "+ text_min_degree_text

                        (context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(
                                intent!!.getIntExtra("notificationId", 0),
                                Notification.Builder(context).apply {
                                    setSmallIcon(android.R.drawable.ic_dialog_info)
                                    setContentTitle(shareBody)
                                    setContentText(intent.getCharSequenceExtra("reminder"))
                                    setWhen(System.currentTimeMillis())
                                    setPriority(Notification.PRIORITY_DEFAULT)
                                    setAutoCancel(true)
                                    setDefaults(Notification.DEFAULT_SOUND)
                                    setContentIntent(PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0))
                                }.build()
                        )

                    }
        }
    }
    // Converts to celcius
    private fun convertFahrenheitToCelcius(fahrenheit: Double): Double {
        return (fahrenheit - 32) * 5 / 9
    }

    // Converts to fahrenheit
    private fun convertCelciusToFahrenheit(celsius: Double): Double {
        return celsius * 9 / 5 + 32
    }
}