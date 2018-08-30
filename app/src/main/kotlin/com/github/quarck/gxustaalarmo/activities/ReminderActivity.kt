package com.github.quarck.gxustaalarmo.activities

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.widget.Button
import android.widget.TextView
import com.github.quarck.gxustaalarmo.AlarmsStorage
import com.github.quarck.gxustaalarmo.R
import com.github.quarck.gxustaalarmo.extensions.MINUTE_SECONDS
import com.github.quarck.gxustaalarmo.extensions.setupAlarmClock
import com.github.quarck.gxustaalarmo.extensions.showOverLockscreen
import com.github.quarck.gxustaalarmo.helpers.ALARM_ID
import com.github.quarck.gxustaalarmo.models.Alarm
import java.util.*

class ReminderActivity : Activity() {
    private val INCREASE_VOLUME_DELAY = 3000L

    private val increaseVolumeHandler = Handler()
    private val swipeGuideFadeHandler = Handler()
    private val moreVibrationHandler = Handler()

    private lateinit var alarm: Alarm
    private var mediaPlayer: MediaPlayer? = null
    private var lastVolumeValue = 0.1f

    private var vibratorService: Vibrator? = null
    private val random = Random()

    private var stopState = 0

    private var vibrationPattern = LongArray(64)
    private var writeIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        showOverLockscreen()

        val id = intent.getIntExtra(ALARM_ID, -1)

        alarm = AlarmsStorage(this).getAlarmWithId(id) ?: return

        findViewById<TextView>(R.id.reminder_title).text = alarm.label
        findViewById<TextView>(R.id.reminder_text).text = "Test"

        setupAudio()
        setupVibration()


        findViewById<Button>(R.id.buttonS1).setOnClickListener({
            if (stopState == 0)
                stopState = 1
            else
                stopState = 0
        })
        findViewById<Button>(R.id.buttonS2).setOnClickListener({
            if (stopState == 1)
                stopState = 2
            else
                stopState = 0
        })
        findViewById<Button>(R.id.buttonS3).setOnClickListener({
            if (stopState == 2)
                stopState = 3
            else
                stopState = 0
        })
        findViewById<Button>(R.id.buttonS4).setOnClickListener({
            if (stopState == 3)
                finishActivity()
            else
                stopState = 0
        })
     }

    private fun setupVibration() {
        if (!alarm.vibrate)
            return

        vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibratorService == null)
            return

        vibratorRunAndRunAndRun()
    }

    private fun patternDone(): Boolean {
        return writeIndex >= vibrationPattern.size
    }

    private fun patternAdd(time: Long) {
        if (writeIndex < vibrationPattern.size) {
            vibrationPattern[writeIndex] = time
            writeIndex += 1
        }
    }

    fun randomWaveformFullRandom() {
        var totalLength = 0L
        while (totalLength < SPLICE_LEN) {
            val v = random.nextInt(500) + 100L
            patternAdd(v)
            totalLength += v
        }
    }

    fun randomWaveformManyShorts() {

        var totalLength = 0L
        while (totalLength < SPLICE_LEN) {
            val v = random.nextInt(50) + 100L
            patternAdd(v)
            totalLength += v
        }
//        Pair(totalLength, VibrationEffect.createWaveform(pattern.toLongArray(), -1))
    }

    fun randomWaveformManyLongs() {

        var totalLength = 0L
        while (totalLength < SPLICE_LEN) {
            val v = random.nextInt(2000) + 1500L
            patternAdd(v)
            totalLength += v
        }
//        Pair(totalLength, VibrationEffect.createWaveform(pattern.toLongArray(), -1))
    }

    fun randomWaveformOrganic() {

        var totalLength = 0L
        var v =  random.nextInt(500) + 100L
        while (totalLength < SPLICE_LEN) {
            v += random.nextInt(100) - 50
            if (v < 100L)
                v = 600L
            else if (v > 600L)
                v = 100L

            patternAdd(v)
            totalLength += v
        }

//        Pair(totalLength, VibrationEffect.createWaveform(pattern.toLongArray(), -1))
    }

    fun randomWaveformOrganicLongs() {
//        val pattern = mutableListOf<Long>(0L)

        var totalLength = 0L
        var v =  random.nextInt(500) + 1000L
        while (totalLength < SPLICE_LEN) {
            v += random.nextInt(300) - 150
            if (v < 400L)
                v = 1500L
            else if (v > 1500L)
                v = 400L
            totalLength += v
        }
  //      Pair(totalLength, VibrationEffect.createWaveform(pattern.toLongArray(), -1))
    }

    fun randomWaveformSuperfastPulsar() {

//        val pattern = mutableListOf<Long>(0L)

        var totalLength = 0L
        while (totalLength < SPLICE_LEN) {
            val v = 70L
            patternAdd(v)
            totalLength += v
        }

  //      Pair(totalLength, VibrationEffect.createWaveform(pattern.toLongArray(), -1))
    }

    fun randomWaveformAcceleratingPulsar() {

        // val pattern = mutableListOf<Long>(0L)
        var totalLength = 0L
        var v = 400L
        while (totalLength < SPLICE_LEN) {
            patternAdd(v)
            totalLength += v
            v = (v / 1.4).toLong()
        }
    //    Pair(totalLength, VibrationEffect.createWaveform(pattern.toLongArray(), -1))
    }

    fun randomWaveformDeceleratingPulsar() {

//        val pattern = mutableListOf<Long>(0L)

        var totalLength = 0L
        var v = 100L
        while (totalLength < SPLICE_LEN) {
            patternAdd(v)
            totalLength += v
            v = (v * 1.4).toLong()
        }

//        Pair(totalLength, VibrationEffect.createWaveform(pattern.toLongArray(), -1))
    }

    fun randomWaveUglyBeats() {

//        val pattern = mutableListOf<Long>(0L)

        patternAdd(100); patternAdd(100)
        patternAdd(100); patternAdd(100)
        patternAdd(300); patternAdd(100)
        // 800

        patternAdd(100); patternAdd(100)
        patternAdd(100); patternAdd(100)
        patternAdd(300); patternAdd(100)
        // 1600

        patternAdd(100); patternAdd(100)
        patternAdd(100); patternAdd(100)
        patternAdd(300); patternAdd(100)
        // 2400

        patternAdd(100); patternAdd(100)
        patternAdd(100); patternAdd(100)
        patternAdd(100); patternAdd(100)
        patternAdd(300); patternAdd(100)
        // 3400

        patternAdd(100); patternAdd(100)
        patternAdd(100); patternAdd(100)
        patternAdd(100); patternAdd(100)
        patternAdd(300); patternAdd(100)
        // 4400

///        Pair(pattern.sum(), VibrationEffect.createWaveform(pattern.toLongArray(), -1))
    }


    private fun generateRandomWaveForm() {

        while (!patternDone()) {
            when (random.nextInt(9)) {
                0 -> randomWaveformManyShorts()
                1 -> randomWaveformManyLongs()
                2 -> randomWaveformOrganic()
                3 -> randomWaveformOrganicLongs()
                4 -> randomWaveformSuperfastPulsar()
                5 -> randomWaveformAcceleratingPulsar()
                6 -> randomWaveformDeceleratingPulsar()
                7 -> randomWaveUglyBeats()
                else -> randomWaveformFullRandom()
            }
        }
    }



    private fun vibratorRunAndRunAndRun() {
        generateRandomWaveForm()
        val effect = VibrationEffect.createWaveform(vibrationPattern, 0)
        vibratorService?.vibrate(effect)
    }


    private fun setupAudio() {
        //if (!config.increaseVolumeGradually) {
            lastVolumeValue = 1f
        //}

        val soundUri = Uri.parse(alarm.soundUri)

        mediaPlayer = MediaPlayer().apply {

            val audioAttrib =
                    AudioAttributes.Builder()
                            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                            .setLegacyStreamType(AudioManager.STREAM_ALARM)
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
            setAudioAttributes(audioAttrib)


            try {
                setDataSource(this@ReminderActivity, soundUri)

                setVolume(lastVolumeValue, lastVolumeValue)
                isLooping = true
                prepare()
                start()
            }
            catch (ex: Exception) {
                // no audio then
            }
        }

        //if (config.increaseVolumeGradually) {
         //   scheduleVolumeIncrease()
       // }
    }

    private fun scheduleVolumeIncrease() {
        increaseVolumeHandler.postDelayed({
            lastVolumeValue = Math.min(lastVolumeValue + 0.1f, 1f)
            mediaPlayer?.setVolume(lastVolumeValue, lastVolumeValue)
            scheduleVolumeIncrease()
        }, INCREASE_VOLUME_DELAY)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        finishActivity()
    }

    override fun onDestroy() {
        super.onDestroy()

        increaseVolumeHandler.removeCallbacksAndMessages(null)
        swipeGuideFadeHandler.removeCallbacksAndMessages(null)
        moreVibrationHandler.removeCallbacksAndMessages(null)

        vibratorService?.cancel()

        destroyPlayer()
    }

    private fun destroyPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun snoozeAlarm() {
        destroyPlayer()
        setupAlarmClock(alarm, 1 * MINUTE_SECONDS)
        finishActivity()
    }

    private fun finishActivity() {
        destroyPlayer()
        finish()
        overridePendingTransition(0, 0)
    }

    companion object {
        const val SPLICE_LEN = 4000L
    }
}
