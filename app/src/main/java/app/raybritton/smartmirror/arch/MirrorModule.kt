package app.raybritton.smartmirror.arch

import android.app.Application
import androidx.room.Room
import app.raybritton.smartmirror.BuildConfig
import app.raybritton.smartmirror.data.WeatherManager
import app.raybritton.smartmirror.data.api.DarkSkyService
import app.raybritton.smartmirror.data.api.FirewallService
import app.raybritton.smartmirror.data.api.IpService
import app.raybritton.smartmirror.data.database.MirrorDatabase
import app.raybritton.smartmirror.data.monitors.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

object MirrorModule {
    lateinit var application: Application

    private val database by lazy {
        Room.databaseBuilder(application, MirrorDatabase::class.java, "mirror.db")
            .build()
    }

    private val monitorLogger by lazy { MonitorLogger(database.logDao()) }

    private val updateMonitor: UpdateMonitor by lazy { UpdateMonitorImpl(application, PrefModule.updateAvailable) }

    private val connMonitor: ConnectivityMonitor by lazy {
        ConnectivityMonitorImpl(
            application,
            monitorLogger
        )
    }

    private val deviceMonitor: DeviceMonitor by lazy {
        DeviceMonitorImpl(monitorLogger)
    }

    val logReader: LogReader by lazy {
        monitorLogger
    }

    val monitorManager: MonitorManager by lazy {
        MonitorManager(connMonitor, deviceMonitor, updateMonitor, ipAddressMonitor)
    }

    val darkSkyService: DarkSkyService by lazy {
        darkSkyRetrofit.create(DarkSkyService::class.java)
    }

    val weatherManager by lazy {
        WeatherManager()
    }

    private val firewallService by lazy {
        firewallRetrofit.create(FirewallService::class.java)
    }

    private val ipService by lazy {
        ipRetrofit.create(IpService::class.java)
    }

    val ipAddressMonitor by lazy {
        IpAddressMonitor(firewallService, ipService)
    }

    private val ipRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.IP_URL)
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private val firewallRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.FIREWALL_URL)
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private val darkSkyRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.darksky.net")
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private val okhttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(httpLogger)
            .build()
    }

    private val httpLogger by lazy {
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag("OkHttp").d(message)
            }
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}