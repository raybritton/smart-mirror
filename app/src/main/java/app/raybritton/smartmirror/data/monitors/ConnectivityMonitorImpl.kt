package app.raybritton.smartmirror.data.monitors

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConnectivityMonitorImpl(private val ctx: Application,
                              private val logger: ConnectivityLogger) : ConnectivityMonitor {
    override fun monitor() {
        val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        connectivityManager.registerNetworkCallback(request, callback)
    }

    private val callback = object: ConnectivityManager.NetworkCallback() {
        override fun onUnavailable() {
            logger.addNoNetworkEvent()
        }

        override fun onAvailable(network: Network) {
            logger.addConnectivityEvent(true)
        }

        override fun onLost(network: Network) {
            logger.addConnectivityEvent(false)
        }
    }
}