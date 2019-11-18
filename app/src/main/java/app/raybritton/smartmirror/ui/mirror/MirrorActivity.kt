package app.raybritton.smartmirror.ui.mirror

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.text.format.DateFormat
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.raybritton.smartmirror.R
import app.raybritton.smartmirror.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_mirror.*
import java.util.*

class MirrorActivity : AppCompatActivity() {

    private val powerManager by lazy { getSystemService(Context.POWER_SERVICE) as PowerManager }
    private val dayTimeWakelock by lazy {
        powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "mirror:daytime").apply {
            setReferenceCounted(false)
        }
    }
    private val nightTimeWakelock by lazy {
        powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "mirror:nighttime").apply {
            setReferenceCounted(false)
        }
    }

    private val handler = Handler {
        when (it.what) {
            WHAT_UPDATE -> {
                updateUi()
                true
            }
            WHAT_REFRESH -> {
                refreshPixels()
                true
            }
            WHAT_WAKELOCK -> {
                checkWakelock()
                true
            }
            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mirror)

        handler.sendEmptyMessage(WHAT_UPDATE)
        handler.sendEmptyMessage(WHAT_REFRESH)

        mirror_refresh.setOnClickListener {
            SettingsActivity.start(this)
        }
    }

    override fun onBackPressed() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun onResume() {
        super.onResume()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        handler.sendEmptyMessage(WHAT_WAKELOCK)
    }

    override fun onPause() {
        super.onPause()
        dayTimeWakelock.release()
        nightTimeWakelock.release()
    }

    @SuppressLint("WakelockTimeout") //it's supposed to last all day
    private fun checkWakelock() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (hour > 6 || hour < 21) {
            if (!dayTimeWakelock.isHeld) {
                nightTimeWakelock.release()
                dayTimeWakelock.acquire()
            }
        } else {
            if (!nightTimeWakelock.isHeld) {
                nightTimeWakelock.acquire()
                dayTimeWakelock.release()
            }
        }

        handler.sendEmptyMessageDelayed(
            WHAT_WAKELOCK,
            WAKELOCK_MS
        )
    }

    private fun updateUi() {
        mirror_datetime.update()
        mirror_weather.update()

        handler.sendEmptyMessageDelayed(
            WHAT_UPDATE,
            UPDATE_MS
        )
    }

    private fun refreshPixels() {
        mirror_refresh.refresh()

        handler.sendEmptyMessageDelayed(
            WHAT_REFRESH,
            REFRESH_MS
        )
    }

    companion object {
        private const val WHAT_UPDATE = 100
        private const val WHAT_REFRESH = 101
        private const val WHAT_WAKELOCK = 102
        private const val UPDATE_MS = 500L //500ms
        private const val REFRESH_MS = 5 * 60 * 1000 * 60L //5 hours
        private const val WAKELOCK_MS = 10 * 1000 * 60L //10 mins
    }
}
