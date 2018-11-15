package com.hossam.android.weatherfreakingapp.views

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.RadioButton
import com.hossam.android.weatherfreakingapp.R
import com.hossam.android.weatherfreakingapp.utils.AlarmBroadcastReceiver
import com.hossam.android.weatherfreakingapp.utils.Prefs
import com.hossam.android.weatherfreakingapp.utils.Prefs.Companion.LOCATION
import com.hossam.android.weatherfreakingapp.utils.Prefs.Companion.NOTIFICATION
import com.hossam.android.weatherfreakingapp.utils.Prefs.Companion.TEMPRATURE
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*


class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val x = Prefs(this)

        initiateValues(x)

        clickingNotificationCheckBox(x)
        clickingLocation(x)
        clickingTemperature(x)
    }

    override fun onDestroy() {
        setAlarmManager()
        super.onDestroy()
    }

    private fun initiateValues(x: Prefs) {

        var prefs = x.prefs.getString(Prefs.LOCATION, "")
        location_textview.text = prefs

        prefs = x.prefs.getString(Prefs.TEMPRATURE, "")
        temp_unit_textview.text = prefs

        prefs = x.prefs.getString(Prefs.NOTIFICATION, "")
        if (prefs.equals("yes")) {
            notification_cb.isChecked = true
            weather_notif_textview.text = getString(R.string.enabled)
        } else {
            notification_cb.isChecked = false
            weather_notif_textview.text = getString(R.string.disabled)
        }
    }

    private fun clickingNotificationCheckBox(x: Prefs) {
        notif_container.setOnClickListener {

            notification_cb.isChecked = !notification_cb.isChecked
            val editor = x.prefs.edit()
            if (notification_cb.isChecked) {
                weather_notif_textview.text = getString(R.string.enabled)
                editor.putString(NOTIFICATION, "yes")
                editor.apply()
            } else {
                weather_notif_textview.text = getString(R.string.disabled)
                editor.putString(NOTIFICATION, "no")
                editor.apply()
            }
            setAlarmManager()
        }
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
        } else {

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

    fun clickingLocation(x: Prefs) {
        location_container.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            // ...Irrelevant code for customizing the buttons and title
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.dialogue_edittext, null)
            dialogBuilder.setView(dialogView)
            val editor = x.prefs.edit()
            val editText = dialogView.findViewById(R.id.location_edit_text) as EditText
            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {

                        editor.putString(LOCATION, editText.text.toString())
                        editor.apply()

                        val prefs = x.prefs.getString(Prefs.LOCATION, "")
                        location_textview.text = prefs
                    }

                }
            }

            dialogBuilder.setPositiveButton("YES", dialogClickListener)

            val alertDialog = dialogBuilder.create()
            alertDialog.show()

        }
    }

    fun clickingTemperature(x: Prefs) {
        temp_container.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)

            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.dialogue_temp, null)
            dialogBuilder.setView(dialogView)
            val radioButton1 = dialogView.findViewById(R.id.metric) as RadioButton
            val radioButton2 = dialogView.findViewById(R.id.imperial) as RadioButton
            var prefs = x.prefs.getString(Prefs.TEMPRATURE, "")
            if (prefs.equals(getString(R.string.metric))) {
                radioButton1.isChecked = true
            } else if (prefs.equals(getString(R.string.imperial))) {
                radioButton2.isChecked = true
            }


            val editor = x.prefs.edit()
            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (radioButton1.isChecked) {

                            radioButton2.isChecked = false
                            editor.putString(TEMPRATURE, getString(R.string.metric))
                            editor.apply()

                            temp_unit_textview.text = getString(R.string.metric)

                        } else if (radioButton2.isChecked) {

                            radioButton1.isChecked = false
                            editor.putString(TEMPRATURE, getString(R.string.imperial))
                            editor.apply()

                            temp_unit_textview.text = getString(R.string.imperial)

                        }


                    }

                }
            }
            dialogBuilder.setPositiveButton("YES", dialogClickListener)


            val alertDialog = dialogBuilder.create()
            alertDialog.show()
        }
    }
}
