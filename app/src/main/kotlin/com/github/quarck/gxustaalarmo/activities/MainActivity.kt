package com.github.quarck.gxustaalarmo.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import com.github.quarck.gxustaalarmo.AlarmsStorage
import com.github.quarck.gxustaalarmo.R
import com.github.quarck.gxustaalarmo.extensions.cancelAlarmClock
import com.github.quarck.gxustaalarmo.extensions.scheduleNextAlarm
import com.github.quarck.gxustaalarmo.models.Alarm

class MainActivity : Activity() {

    lateinit var xalarmHour0: EditText
    lateinit var xalarmHour1: EditText
    lateinit var xalarmHour2: EditText
    lateinit var xalarmHour3: EditText
    lateinit var xalarmMinute0: EditText
    lateinit var xalarmMinute1: EditText
    lateinit var xalarmMinute2: EditText
    lateinit var xalarmMinute3: EditText
    lateinit var xalarmEnable0: Switch
    lateinit var xalarmEnable1: Switch
    lateinit var xalarmEnable2: Switch
    lateinit var xalarmEnable3: Switch

    lateinit var settings: AlarmsStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // just get a reference to the database to make sure it is created properly
        setContentView(R.layout.activity_main)

        xalarmHour0 = findViewById<EditText>(R.id.alarmHour0)
        xalarmHour1 = findViewById<EditText>(R.id.alarmHour1)
        xalarmHour2 = findViewById<EditText>(R.id.alarmHour2)
        xalarmHour3 = findViewById<EditText>(R.id.alarmHour3)

        xalarmMinute0 = findViewById<EditText>(R.id.alarmMinute0)
        xalarmMinute1 = findViewById<EditText>(R.id.alarmMinute1)
        xalarmMinute2 = findViewById<EditText>(R.id.alarmMinute2)
        xalarmMinute3 = findViewById<EditText>(R.id.alarmMinute3)

        xalarmEnable0 = findViewById<Switch>(R.id.alarmEnable0)
        xalarmEnable1 = findViewById<Switch>(R.id.alarmEnable1)
        xalarmEnable2 = findViewById<Switch>(R.id.alarmEnable2)
        xalarmEnable3 = findViewById<Switch>(R.id.alarmEnable3)

        settings = AlarmsStorage(this)

        xalarmHour0.setText(settings.alarmHour0.toString())
        xalarmHour1.setText(settings.alarmHour1.toString())
        xalarmHour2.setText(settings.alarmHour2.toString())
        xalarmHour3.setText(settings.alarmHour3.toString())

        xalarmMinute0.setText(settings.alarmMinute0.toString())
        xalarmMinute1.setText(settings.alarmMinute1.toString())
        xalarmMinute2.setText(settings.alarmMinute2.toString())
        xalarmMinute3.setText(settings.alarmMinute3.toString())

        xalarmEnable0.isChecked = settings.alarmEnabled0
        xalarmEnable1.isChecked = settings.alarmEnabled1
        xalarmEnable2.isChecked = settings.alarmEnabled2
        xalarmEnable3.isChecked = settings.alarmEnabled3

//        alarmHour0.setOnClickListener(::onClick0)
//        alarmHour1.setOnClickListener(::onClick1)
//        alarmHour2.setOnClickListener(::onClick2)
//        alarmHour3.setOnClickListener(::onClick3)
//
//        alarmMinute0.setOnClickListener(::onClick0)
//        alarmMinute1.setOnClickListener(::onClick1)
//        alarmMinute2.setOnClickListener(::onClick2)
//        alarmMinute3.setOnClickListener(::onClick3)

        xalarmEnable0.setOnCheckedChangeListener({ v, b -> onClick0(v)})
        xalarmEnable1.setOnCheckedChangeListener({ v, b -> onClick1(v)})
        xalarmEnable2.setOnCheckedChangeListener({ v, b -> onClick2(v)})
        xalarmEnable3.setOnCheckedChangeListener({ v, b -> onClick3(v)})

//        if (getNextAlarm().isEmpty()) {
//            Thread {
//                rescheduleEnabledAlarms()
//            }.start()
//        }
    }

    private fun checkAlarmState(alarm: Alarm?) {

        if (alarm == null)
            return

        if (alarm.isEnabled) {
            this.scheduleNextAlarm(alarm, true)
        } else {
            this.cancelAlarmClock(alarm)
        }
    }

    fun onClick0(v: View) {

        settings.alarmHour0 = xalarmHour0.text.toString().toInt()
        settings.alarmMinute0 = xalarmMinute0.text.toString().toInt()
        settings.alarmEnabled0 = (v as Switch).isChecked

        checkAlarmState(settings.getAlarmWithId(0))
    }

    fun onClick1(v: View) {
        settings.alarmHour1 = xalarmHour1.text.toString().toInt()
        settings.alarmMinute1 = xalarmMinute1.text.toString().toInt()
        settings.alarmEnabled1 = xalarmEnable1.isChecked
        checkAlarmState(settings.getAlarmWithId(1))
    }


    fun onClick2(v: View) {
        settings.alarmHour2 = xalarmHour2.text.toString().toInt()
        settings.alarmMinute2 = xalarmMinute2.text.toString().toInt()
        settings.alarmEnabled2 = xalarmEnable2.isChecked
        checkAlarmState(settings.getAlarmWithId(2))
    }


    fun onClick3(v: View) {
        settings.alarmHour3 = xalarmHour3.text.toString().toInt()
        settings.alarmMinute3 = xalarmMinute3.text.toString().toInt()
        settings.alarmEnabled3 = xalarmEnable3.isChecked
        checkAlarmState(settings.getAlarmWithId(3))
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onNewIntent(intent: Intent) {
    }
}
