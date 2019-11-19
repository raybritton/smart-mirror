package app.raybritton.smartmirror.data.monitors

import app.raybritton.smartmirror.data.models.Event
import app.raybritton.smartmirror.data.database.StatusLogDao
import org.joda.time.DateTime
import timber.log.Timber

class MonitorLogger(private val logDao: StatusLogDao): ConnectivityLogger, LogReader, DeviceLogger, PowerLogger {
    override suspend fun addNoNetworkEvent() {
        val event = Event.create(Event.Type.NET, "No WiFi networks available", true)
        Timber.d("Logging $event")
        return logDao.add(event)
    }

    override suspend fun addConnectivityEvent(connected: Boolean) {
        val message = if (connected) "WiFi network connected" else "WiFi network disconnected"
        val event = Event.create(Event.Type.NET, message, !connected)
        Timber.d("Logging $event")
        return logDao.add(event)
    }

    override suspend fun getLog(day: DateTime): List<Event> {
        return logDao.getEventsForDay(day.toString())
    }

    override suspend fun addStartEvent() {
        val event = Event.create(Event.Type.POWER, "App started", false)
        Timber.d("Logging $event")
        return logDao.add(event)
    }

}