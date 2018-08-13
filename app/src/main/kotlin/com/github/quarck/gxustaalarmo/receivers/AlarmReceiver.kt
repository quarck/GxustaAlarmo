package com.github.quarck.gxustaalarmo.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.quarck.gxustaalarmo.AlarmsStorage
import com.github.quarck.gxustaalarmo.activities.ReminderActivity
import com.github.quarck.gxustaalarmo.helpers.ALARM_ID

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra(ALARM_ID, -1)
        val alarm = AlarmsStorage(context).getAlarmWithId(id) ?: return

        Intent(context, ReminderActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(ALARM_ID, id)
            context.startActivity(this)
        }
    }
}
