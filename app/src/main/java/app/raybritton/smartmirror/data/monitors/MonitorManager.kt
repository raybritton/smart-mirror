package app.raybritton.smartmirror.data.monitors

class MonitorManager(
    private val connectivityMonitor: ConnectivityMonitor,
    private val deviceMonitor: DeviceMonitor,
    private val updateMonitor: UpdateMonitor,
    private val ipAddressMonitor: IpAddressMonitor
) {
    fun start() {
        connectivityMonitor.monitor()
        deviceMonitor.monitor()
        updateMonitor.monitor()
        ipAddressMonitor.monitor()
    }
}