package app.raybritton.smartmirror.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import app.raybritton.smartmirror.R
import app.raybritton.smartmirror.arch.MirrorModule
import kotlinx.android.synthetic.main.activity_log_status.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import timber.log.Timber

class LogStatusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_status)

        val adapter = LogAdapter(this)

        log_list.layoutManager = LinearLayoutManager(this)
        log_list.itemAnimator = DefaultItemAnimator()
        log_list.adapter = adapter

        GlobalScope.launch {
            val events = MirrorModule.logReader.getLog(DateTime.now())
            Timber.d("Loaded ${events.size} events")
            adapter.data = events
        }
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, LogStatusActivity::class.java))
        }
    }
}