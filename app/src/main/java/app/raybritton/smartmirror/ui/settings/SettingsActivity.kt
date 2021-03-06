package app.raybritton.smartmirror.ui.settings

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.raybritton.elog.ELog
import app.raybritton.smartmirror.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        window.decorView.keepScreenOn = true

        settings_log.setOnClickListener {
            LogStatusActivity.start(this)
        }

        settings_apps.setOnClickListener {
            AppActivity.start(this)
        }

        settings_crash_logs.setOnClickListener {
            ELog.openLogScreen()
        }
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SettingsActivity::class.java))
        }
    }
}
