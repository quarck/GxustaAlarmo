package com.github.quarck.gxustaalarmo.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.quarck.gxustaalarmo.extensions.rescheduleEnabledAlarms

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.rescheduleEnabledAlarms()
    }
}
