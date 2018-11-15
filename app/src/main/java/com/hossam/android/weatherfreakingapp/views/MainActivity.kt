package com.hossam.android.weatherfreakingapp.views

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.menu.MenuBuilder
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.hossam.android.weatherfreakingapp.R
import com.hossam.android.weatherfreakingapp.WeatherAdapter
import com.hossam.android.weatherfreakingapp.modelskotlin.ListItem
import com.hossam.android.weatherfreakingapp.utils.AlarmBroadcastReceiver
import com.hossam.android.weatherfreakingapp.utils.Prefs
import com.hossam.android.weatherfreakingapp.utils.WebServiceEngine
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var x : ListItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getRecyclerViewReady()
        fetchData()
        setupToolBar()

        setAlarmManager()

    }
    var notificationId = 0


    private fun setAlarmManager() {
        val xx = Prefs(this)
        var prefs = xx.prefs.getString(Prefs.NOTIFICATION, "")
            if (prefs.equals("yes")) {
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DATE, 1)
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.apply {
                            set(Calendar.HOUR_OF_DAY, 8)
                        }.timeInMillis, 86000, PendingIntent.getBroadcast(
                        applicationContext,
                        101,
                        Intent(applicationContext, AlarmBroadcastReceiver::class.java).apply {
                            putExtra("notificationId", notificationId)
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT
                )
                )
            }else{

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val pendingIntent = PendingIntent.getBroadcast(
                        applicationContext,
                        101,
                        Intent(applicationContext, AlarmBroadcastReceiver::class.java).apply {
                            putExtra("notificationId", notificationId)
                        },
                        PendingIntent.FLAG_CANCEL_CURRENT)

                alarmManager.cancel(pendingIntent)
            }


    }

    private fun setupToolBar() {

        setSupportActionBar(mainToolbar)
    }


    override fun onResume() {
        super.onResume()
        setAlarmManager()
        val xx = Prefs(this)
        var prefs = xx.prefs.getString(Prefs.TEMPRATURE, "")
        if(x!=null) {
            if (prefs.equals(getString(R.string.imperial))) {
                text_min_degree.text = Math.round(convertCelciusToFahrenheit(x?.temp?.min!!)).toString()
                text_max_degree.text = Math.round(convertCelciusToFahrenheit(x?.temp?.max!!)).toString()
            } else if (prefs.equals(getString(R.string.metric))) {
                text_min_degree.text = Math.round(convertFahrenheitToCelcius(x?.temp?.min!!)).toString()
                text_max_degree.text = Math.round(convertFahrenheitToCelcius(x?.temp?.max!!)).toString()
            }
            recyclerview.adapter.notifyDataSetChanged()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            if (menu is MenuBuilder) {
                try {
                    val field = menu.javaClass.getDeclaredField("mOptionalIconsVisible")
                    field.isAccessible = true
                    field.setBoolean(menu, true)
                } catch (ignored: Exception) {
                    // ignored exception
                }
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.map_item -> {
                val intent =  Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps"));
                startActivity(intent);
                return true
            }
            R.id.settings_item -> {
                val intent =  Intent(this, SettingsActivity::class.java);
                startActivity(intent);
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getRecyclerViewReady() {
        val layoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager
        recyclerview.hasFixedSize()
        recyclerview.setNestedScrollingEnabled(false);
    }

    private fun fetchData() {

        WebServiceEngine.getRetrofit(this).getWeatherData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {}
                .doFinally { }
                .doOnError { }
                .subscribe { e ->
                    var list: MutableList<ListItem?>? = e.list as MutableList<ListItem?>?
                     x = list!!.get(0)
                    single_item_container.setOnClickListener {

                        val intent = Intent(this, DetailActivity::class.java).putExtra("Model", x)
                        this.startActivity(intent)
                    }
                    weather_icon_text.text = x?.weather?.get(0)?.description

                    val xx = Prefs(this)
                    var prefs = xx.prefs.getString(Prefs.TEMPRATURE, "")
                    if(prefs.equals(getString(R.string.imperial))){
                        text_min_degree.text = Math.round(convertCelciusToFahrenheit(x?.temp?.min!!)).toString()
                        text_max_degree.text = Math.round(convertCelciusToFahrenheit(x?.temp?.max!!)).toString()
                    }else if(prefs.equals(getString(R.string.metric))){
                        text_min_degree.text = Math.round(convertFahrenheitToCelcius(x?.temp?.min!!)).toString()
                        text_max_degree.text = Math.round(convertFahrenheitToCelcius(x?.temp?.max!!)).toString()
                    }

                    val cal = Calendar.getInstance()
                    val format1 = SimpleDateFormat("dd MMM YYYY")
                    var yourfinaldate: String = this.resources.getString(R.string.today) + " " + format1.format(cal.getTime());

                    today_text.text = yourfinaldate

                    list.removeAt(0);
                    recyclerview.adapter = WeatherAdapter(this, e.list)

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
