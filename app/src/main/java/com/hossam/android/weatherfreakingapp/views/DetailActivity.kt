package com.hossam.android.weatherfreakingapp.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.menu.MenuBuilder
import android.view.Menu
import android.view.MenuItem
import com.hossam.android.weatherfreakingapp.R
import com.hossam.android.weatherfreakingapp.modelskotlin.ListItem
import com.hossam.android.weatherfreakingapp.utils.Prefs
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    lateinit var weatheritem: ListItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)




        val x = Prefs(this)
        var prefs = x.prefs.getString(Prefs.TEMPRATURE, "Enabled")

        if (intent.hasExtra("Model")) {
            weatheritem = intent.getParcelableExtra("Model")
            humidity_text.text = Math.round(weatheritem.humidity!!).toString() + " %"

            weather_icon_text.text = weatheritem.weather?.get(0)?.description

            if(prefs.equals(getString(R.string.imperial))){
                text_min_degree.text = Math.round(convertCelciusToFahrenheit(weatheritem.temp?.min!!)).toString()
                text_max_degree.text = Math.round(convertCelciusToFahrenheit(weatheritem.temp?.max!!)).toString()
            }else if(prefs.equals(getString(R.string.metric))){
                text_min_degree.text = Math.round(convertFahrenheitToCelcius(weatheritem.temp?.min!!)).toString()
                text_max_degree.text = Math.round(convertFahrenheitToCelcius(weatheritem.temp?.max!!)).toString()
            }
            val cal = Calendar.getInstance()
            val format1 = SimpleDateFormat("dd MMM YYYY")
            var yourfinaldate: String = this.resources.getString(R.string.today) + " " + format1.format(cal.getTime());

            today_text.text = yourfinaldate
            pressure_text.text = Math.round(weatheritem.pressure!!).toString()+" hPa"
            wind_text.text = Math.round(weatheritem.speed!!).toString() + " mph E"
        }


        setupToolBar()
    }

    private fun setupToolBar() {

        setSupportActionBar(mainToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        getSupportActionBar()?.setHomeButtonEnabled(true);

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menudetail, menu)
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
            R.id.share_item -> {
                val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                var shareBody = String()
                shareBody= today_text.text.toString()+" MaxDegree : " + text_max_degree.text.toString()+" MinDegree : "+ text_min_degree.text.toString()
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here")
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
                startActivity(Intent.createChooser(sharingIntent, "Share via"))
                return true
            }
            R.id.settings_menu -> {
                val intent =  Intent(this, SettingsActivity::class.java);
                startActivity(intent);
                return true
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        val xx = Prefs(this)
        var prefs = xx.prefs.getString(Prefs.TEMPRATURE, "")
        if(weatheritem!=null) {
            if (prefs.equals(getString(R.string.imperial))) {
                text_min_degree.text = Math.round(convertCelciusToFahrenheit(weatheritem?.temp?.min!!)).toString()
                text_max_degree.text = Math.round(convertCelciusToFahrenheit(weatheritem?.temp?.max!!)).toString()
            } else if (prefs.equals(getString(R.string.metric))) {
                text_min_degree.text = Math.round(convertFahrenheitToCelcius(weatheritem?.temp?.min!!)).toString()
                text_max_degree.text = Math.round(convertFahrenheitToCelcius(weatheritem?.temp?.max!!)).toString()
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
