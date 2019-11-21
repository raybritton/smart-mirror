package app.raybritton.smartmirror.data.monitors

class MonitorManager(
    private val connectivityMonitor: ConnectivityMonitor,
    private val deviceMonitor: DeviceMonitor
) {
    fun start() {
        connectivityMonitor.monitor()
        deviceMonitor.monitor()
    }
}