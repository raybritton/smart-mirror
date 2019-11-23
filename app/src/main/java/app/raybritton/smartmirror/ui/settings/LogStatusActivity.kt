package app.raybritton.smartmirror.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import app.raybritton.smartmirror.R
import app.raybritton.smartmirror.ext.isToday
import app.raybritton.smartmirror.ext.isYesterday
import app.raybritton.smartmirror.ui.arch.BaseActivity
import kotlinx.android.synthetic.main.activity_log_status.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.time.format.DateTimeFormatter

class LogStatusActivity : BaseActivity<LogStatusViewModel>(LogStatusViewModel::class.java) {
    private val formatter = DateTimeFormat.forPattern("dd/MM/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_status)

        val adapter = LogAdapter(this) {
            viewModel.latestEventRead()
        }

        log_list.layoutManager = LinearLayoutManager(this)
        log_list.itemAnimator = DefaultItemAnimator()
        log_list.adapter = adapter

        viewModel.unreadEventId.observe { id ->
            adapter.latestUnreadEventId = id
        }

        viewModel.logs.observe { result ->
            log_date.text = formatDate(result.dateTime)
            updateButtons(result.dateTime)
            when (result) {
                is Result.Success -> {
                    adapter.data = result.logs
                }
                is Result.Error -> {
                    adapter.data = emptyList()
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(result.message)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }
            }

        }
    }

    private fun updateButtons(dateTime: DateTime) {
        log_next.isEnabled = !dateTime.isToday()

        log_next.setOnClickListener {
            viewModel.load(dateTime.plusDays(1))
        }

        log_prev.setOnClickListener {
            viewModel.load(dateTime.minusDays(1))
        }
    }

    private fun formatDate(dateTime: DateTime): String {
        if (dateTime.isToday()) {
            return getString(R.string.date_today)
        } else if (dateTime.isYesterday()) {
            return getString(R.string.date_yesterday)
        } else {
            return formatter.print(dateTime)
        }
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, LogStatusActivity::class.java))
        }
    }
}