package app.raybritton.smartmirror.data.monitors

import app.raybritton.elog.ELog
import app.raybritton.smartmirror.data.models.Event
import app.raybritton.smartmirror.data.database.StatusLogDao
import app.raybritton.smartmirror.ext.applyIoSchedulers
import io.reactivex.Flowable
import io.reactivex.Single
import org.joda.time.DateTime
import timber.log.Timber

class MonitorLogger(private val logDao: StatusLogDao): ConnectivityLogger, LogReader, DeviceLogger, PowerLogger {
    private val errorHandler:(Throwable) -> Unit = {ex ->
        Timber.e(ex, "Network event")
        ELog.submitCurrentLogSilently("Network event", false)
    }

    override fun addNoNetworkEvent() {
        val event = Event.create(Event.Type.NET, "No WiFi networks available", true)
        Timber.d("Logging $event")
        logDao.add(event).applyIoSchedulers().subscribe({},errorHandler)
    }

    override fun addConnectivityEvent(connected: Boolean) {
        val message = if (connected) "WiFi network connected" else "WiFi network disconnected"
        val event = Event.create(Event.Type.NET, message, !connected)
        Timber.d("Logging $event")
        logDao.add(event).applyIoSchedulers().subscribe({},errorHandler)
    }

    override fun getLog(day: DateTime): Single<List<Event>> {
        return logDao.getEventsForDay(day.toString())
    }

    override fun hasAnyImportantLogs(): Flowable<Boolean> {
        return logDao.getImportantEventsForDay(DateTime.now().toString())
            .map { it.isNotEmpty() }
    }

    override fun addStartEvent() {
        val event = Event.create(Event.Type.APP, "App started", false)
        Timber.d("Logging $event")
        logDao.add(event).applyIoSchedulers().subscribe({},errorHandler)
    }

}