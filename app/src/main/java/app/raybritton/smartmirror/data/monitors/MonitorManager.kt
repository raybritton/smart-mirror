package app.raybritton.smartmirror.data.monitors

class MonitorManager(
    private val connectivityMonitor: ConnectivityMonitor,
    private val deviceMonitor: DeviceMonitor,
    private val powerMonitor: PowerMonitor
) {
    fun start() {
        connectivityMonitor.monitor()
        deviceMonitor.monitor()
        powerMonitor.monitor()
    }
}