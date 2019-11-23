package app.raybritton.smartmirror.data.monitors

class DeviceMonitorImpl(private val logger: DeviceLogger) : DeviceMonitor {
    override fun monitor() {
        logger.addStartEvent()
    }

}