package com.github.quarck.gxustaalarmo.extensions

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.github.quarck.gxustaalarmo.AlarmsStorage
import com.github.quarck.gxustaalarmo.activities.MainActivity
import com.github.quarck.gxustaalarmo.helpers.*
import com.github.quarck.gxustaalarmo.models.Alarm
import com.github.quarck.gxustaalarmo.receivers.AlarmReceiver
import java.util.*

const val PACKAGE_NAME = "com.github.quarck.gxustaalarmo"

fun Context.scheduleNextAlarm(alarm: Alarm, showToast: Boolean) {
    val cal = Calendar.getInstance()
    val currentTimeInMinutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)

    var triggerTime = (alarm.timeInMinutes - currentTimeInMinutes)*60 - cal.get(Calendar.SECOND)

    if (alarm.timeInMinutes <= currentTimeInMinutes) {
        triggerTime += DAY_MINUTES * 60
    }

    setupAlarmClock(alarm, triggerTime)

    val alarmMins = triggerTime / 60

    Toast.makeText(this, "Next alarm in ${alarmMins / 60} hours ${alarmMins % 60} minutes", Toast.LENGTH_LONG).show()
}

fun Context.showRemainingTimeMessage(totalMinutes: Int) {
//    val fullString = String.format(getString(R.string.alarm_goes_off_in), formatMinutesToTimeString(totalMinutes))
//    toast(fullString, Toast.LENGTH_LONG)
}

fun Context.setupAlarmClock(alarm: Alarm, triggerInSeconds: Int) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val targetMS = System.currentTimeMillis() + triggerInSeconds * 1000
    val info = AlarmManager.AlarmClockInfo(targetMS, getOpenAlarmTabIntent())
    alarmManager.setAlarmClock(info,  getAlarmIntent(alarm))
}

fun Context.getLaunchIntent() = packageManager.getLaunchIntentForPackage(PACKAGE_NAME)

fun Context.getOpenAlarmTabIntent(): PendingIntent {
    val intent = getLaunchIntent() ?: Intent(this, MainActivity::class.java)
    intent.putExtra(OPEN_TAB, TAB_ALARM)
    return PendingIntent.getActivity(this, OPEN_ALARMS_TAB_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}

fun Context.getAlarmIntent(alarm: Alarm): PendingIntent {
    val intent = Intent(this, AlarmReceiver::class.java)
    intent.putExtra(ALARM_ID, alarm.id)
    return PendingIntent.getBroadcast(this, alarm.id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}

fun Context.cancelAlarmClock(alarm: Alarm) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(getAlarmIntent(alarm))
}

fun Context.hideNotification(id: Int) {
    val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.cancel(id)
}


fun Context.rescheduleEnabledAlarms() {

    val storage = AlarmsStorage(this)
    storage.alarm0.let { if (it.isEnabled) scheduleNextAlarm(it, false) }
    storage.alarm1.let { if (it.isEnabled) scheduleNextAlarm(it, false) }
    storage.alarm2.let { if (it.isEnabled) scheduleNextAlarm(it, false) }
    storage.alarm3.let { if (it.isEnabled) scheduleNextAlarm(it, false) }
}

