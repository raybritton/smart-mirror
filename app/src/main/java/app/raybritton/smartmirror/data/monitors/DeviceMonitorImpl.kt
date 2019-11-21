package app.raybritton.smartmirror.data.monitors

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.log

class DeviceMonitorImpl(private val logger: DeviceLogger) : DeviceMonitor {
    override fun monitor() {
        logger.addStartEvent()
    }

}