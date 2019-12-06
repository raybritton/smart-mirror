package app.raybritton.smartmirror.ui.mirror

import androidx.lifecycle.MutableLiveData
import app.raybritton.elog.ELog
import app.raybritton.smartmirror.arch.MirrorModule
import app.raybritton.smartmirror.data.WeatherManager
import app.raybritton.smartmirror.data.monitors.IpAddressMonitor
import app.raybritton.smartmirror.ext.applyIoSchedulers
import app.raybritton.smartmirror.ui.arch.BaseViewModel
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class MirrorViewModel(weatherManager: WeatherManager = MirrorModule.weatherManager,
                      ipAddressMonitor: IpAddressMonitor = MirrorModule.ipAddressMonitor) : BaseViewModel() {

    val hasFailed = MutableLiveData<Boolean>(false)

    init {
        weatherManager.watchSystemFailure()
            .applyIoSchedulers()
            .subscribe({
                hasFailed.value = true
            },{
                Timber.e(it, "System failure failed (weather)")
                ELog.submitCurrentLogSilently("System failure failed (weather)", false)
                hasFailed.value = true
            }).addTo(this.disposable)

        ipAddressMonitor.watchSystemFailure()
            .applyIoSchedulers()
            .subscribe({
                hasFailed.value = true
            },{
                Timber.e(it, "System failure failed (ip)")
                ELog.submitCurrentLogSilently("System failure failed (ip)", false)
                hasFailed.value = true
            }).addTo(this.disposable)
    }
}