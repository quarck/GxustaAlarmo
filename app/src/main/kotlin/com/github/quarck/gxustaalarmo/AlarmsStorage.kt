
package com.github.quarck.gxustaalarmo

import android.content.Context
import com.github.quarck.gxustaalarmo.models.Alarm

class AlarmsStorage(private val ctx: Context) : PersistentStorageBase(ctx, PREFS_NAME) {

    fun getAlarmWithId(id: Int): Alarm? {
        when (id) {
            0 -> return alarm0
            1 -> return alarm1
            2 -> return alarm2
            3 -> return alarm3
            else -> return null
        }
    }

    fun setAlarm(id: Int, enable: Boolean) {

        when (id) {
            0 -> alarmEnabled0 = enable
            1 -> alarmEnabled1 = enable
            2 -> alarmEnabled2 = enable
            3 -> alarmEnabled3 = enable
            else -> throw Exception("Unknown alarm")
        }
    }

    var alarmEnabled0 by BooleanProperty(false)
    var alarmHour0 by IntProperty(0)
    var alarmMinute0 by IntProperty(0)
    var alarmNextExpected0 by LongProperty(0)

    var alarmEnabled1 by BooleanProperty(false)
    var alarmHour1 by IntProperty(0)
    var alarmMinute1 by IntProperty(0)
    var alarmNextExpected1 by LongProperty(0)

    var alarmEnabled2 by BooleanProperty(false)
    var alarmHour2 by IntProperty(0)
    var alarmMinute2 by IntProperty(0)
    var alarmNextExpected2 by LongProperty(0)

    var alarmEnabled3 by BooleanProperty(false)
    var alarmHour3 by IntProperty(0)
    var alarmMinute3 by IntProperty(0)
    var alarmNextExpected3 by LongProperty(0)

    var alarm0: Alarm
        get() = Alarm(0, alarmHour0*60 + alarmMinute0, alarmEnabled0, true, "none", "", "Alarm0")
        set(value) {
            alarmHour0 = value.timeInMinutes / 60
            alarmMinute0 = value.timeInMinutes % 60
            alarmEnabled0 = value.isEnabled
        }

    var alarm1: Alarm
        get() = Alarm(1, alarmHour1*60 + alarmMinute1, alarmEnabled1, true, "none", "", "Alarm1")
        set(value) {
            alarmHour1 = value.timeInMinutes / 60
            alarmMinute1 = value.timeInMinutes % 60
            alarmEnabled1 = value.isEnabled
        }

    var alarm2: Alarm
        get() = Alarm(2, alarmHour2*60 + alarmMinute2, alarmEnabled2, true, "none", "", "Alarm2")
        set(value) {
            alarmHour2 = value.timeInMinutes / 60
            alarmMinute2 = value.timeInMinutes % 60
            alarmEnabled2 = value.isEnabled
        }

    var alarm3: Alarm
        get() = Alarm(3, alarmHour3*60 + alarmMinute3, alarmEnabled3, true, "none", "", "Alarm3")
        set(value) {
            alarmHour3 = value.timeInMinutes / 60
            alarmMinute3 = value.timeInMinutes % 60
            alarmEnabled3 = value.isEnabled
        }

    companion object {
        const val PREFS_NAME: String = "alarms_state"
    }
}

val Context.alarmStorage: AlarmsStorage
    get() = AlarmsStorage(this)
