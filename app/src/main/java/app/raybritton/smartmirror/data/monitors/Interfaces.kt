package app.raybritton.smartmirror.data.monitors

import app.raybritton.smartmirror.data.models.Event
import org.joda.time.DateTime

interface LogReader {
    suspend fun getLog(day: DateTime): List<Event>
}

interface ConnectivityMonitor {
    fun monitor()
}

interface ConnectivityLogger {
    suspend fun addNoNetworkEvent()
    suspend fun addConnectivityEvent(connected: Boolean)
}

interface DeviceMonitor {
    fun monitor()
}

interface DeviceLogger {
    suspend fun addStartEvent()
}

interface PowerMonitor {
    fun monitor()
}

interface PowerLogger {
}