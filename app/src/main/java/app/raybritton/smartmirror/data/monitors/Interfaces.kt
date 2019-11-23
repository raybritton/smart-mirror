package app.raybritton.smartmirror.data.monitors

import app.raybritton.smartmirror.data.models.Event
import io.reactivex.Single
import org.joda.time.DateTime

interface LogReader {
    fun getLog(day: DateTime): Single<List<Event>>
}

interface ConnectivityMonitor {
    fun monitor()
}

interface ConnectivityLogger {
    fun addNoNetworkEvent()
    fun addConnectivityEvent(connected: Boolean)
}

interface DeviceMonitor {
    fun monitor()
}

interface DeviceLogger {
    fun addStartEvent()
}