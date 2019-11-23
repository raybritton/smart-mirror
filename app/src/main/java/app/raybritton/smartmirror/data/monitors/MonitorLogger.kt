package app.raybritton.smartmirror.data.monitors

import android.annotation.SuppressLint
import app.raybritton.elog.ELog
import app.raybritton.smartmirror.arch.PrefModule
import app.raybritton.smartmirror.data.models.Event
import app.raybritton.smartmirror.data.database.StatusLogDao
import app.raybritton.smartmirror.ext.applyIoSchedulers
import com.f2prateek.rx.preferences2.Preference
import io.reactivex.Flowable
import io.reactivex.Single
import org.joda.time.DateTime
import timber.log.Timber

@SuppressLint("CheckResult")
class MonitorLogger(private val logDao: StatusLogDao,
                    private val unreadImportantEventId: Preference<Long> = PrefModule.latestUnreadImportantEventId) : ConnectivityLogger, LogReader, DeviceLogger, PowerLogger {
    private val errorHandler: (Throwable) -> Unit = { ex ->
        Timber.e(ex, "Network event")
        ELog.submitCurrentLogSilently("Network event", false)
    }

    override fun addNoNetworkEvent() {
        val event = Event.create(Event.Type.NET, "No WiFi networks available", true)
        Timber.d("Logging $event")
        logDao.add(event).applyIoSchedulers().subscribe({
            unreadImportantEventId.set(it)
        }, errorHandler)
    }

    override fun addConnectivityEvent(connected: Boolean) {
        val message = if (connected) "WiFi network connected" else "WiFi network disconnected"
        val event = Event.create(Event.Type.NET, message, !connected)
        Timber.d("Logging $event")
        logDao.add(event).applyIoSchedulers().subscribe({
            if (!connected) {
                unreadImportantEventId.set(it)
            }
        }, errorHandler)
    }

    override fun getLog(day: DateTime): Single<List<Event>> {
        return logDao.getEventsForDay(day.toString())
    }


    override fun addStartEvent() {
        val event = Event.create(Event.Type.APP, "App started", false)
        Timber.d("Logging $event")
        logDao.add(event).applyIoSchedulers().subscribe({}, errorHandler)
    }

}