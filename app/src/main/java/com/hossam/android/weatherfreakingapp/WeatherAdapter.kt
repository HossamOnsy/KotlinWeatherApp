package com.hossam.android.weatherfreakingapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hossam.android.weatherfreakingapp.modelskotlin.ListItem
import com.hossam.android.weatherfreakingapp.utils.Prefs
import com.hossam.android.weatherfreakingapp.views.DetailActivity
import kotlinx.android.synthetic.main.weather_item_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter(var c: Context, var lists: List<ListItem?>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(c).inflate(R.layout.weather_item_layout, parent, false)
        return Item(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Item).bindData(lists!![position], c, position)
    }


    override fun getItemCount(): Int {
        return lists!!.size
    }


    class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SimpleDateFormat")
        fun bindData(_list: ListItem?, c: Context, position: Int) {
            val cal = Calendar.getInstance()
            val format1 = SimpleDateFormat("EEEE dd MMM YYYY");
            val format2 = SimpleDateFormat("dd MMM YYYY");
            cal.add(Calendar.DATE, position);
            var yourfinaldate: String = format1.format(cal.getTime())

            if (position == 0) {
                yourfinaldate = c.getString(R.string.tomorrow) + " " + format2.format(cal.getTime());
                itemView.date_of_weather_item.text = yourfinaldate
            } else {
                yourfinaldate = format1.format(cal.getTime());
                itemView.date_of_weather_item.text = yourfinaldate
            }

            itemView.type_of_weather_item.text = _list?.weather?.get(0)?.main

            val x = Prefs(c)
            var prefs = x.prefs.getString(Prefs.TEMPRATURE, "")
            if (prefs.equals(c.getString(R.string.imperial))) {
                itemView.min_of_weather_item.text = Math.round(convertCelciusToFahrenheit(_list?.temp?.min!!)).toString()
                itemView.max_of_weather_item.text = Math.round(convertCelciusToFahrenheit(_list?.temp?.max!!)).toString()
            } else if (prefs.equals(c.getString(R.string.metric))) {
                itemView.min_of_weather_item.text = Math.round(convertFahrenheitToCelcius(_list?.temp?.min!!)).toString()
                itemView.max_of_weather_item.text = Math.round(convertFahrenheitToCelcius(_list?.temp?.max!!)).toString()
            }

            itemView.setOnClickListener {
                val intent = Intent(c, DetailActivity::class.java).putExtra("Model", _list)
                c.startActivity(intent)
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


}