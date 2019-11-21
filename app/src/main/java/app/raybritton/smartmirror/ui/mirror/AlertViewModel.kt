package app.raybritton.smartmirror.ui.mirror

import androidx.lifecycle.MutableLiveData
import app.raybritton.elog.ELog
import app.raybritton.smartmirror.arch.MirrorModule
import app.raybritton.smartmirror.data.monitors.LogReader
import app.raybritton.smartmirror.ext.applyIoSchedulers
import app.raybritton.smartmirror.ui.arch.BaseViewModel
import app.raybritton.smartmirror.ui.mirror.weather.WeatherStatus
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class AlertViewModel(logReader: LogReader = MirrorModule.logReader) : BaseViewModel() {
    val hasAlert = MutableLiveData<Boolean>(false)

    init {
        logReader.hasAnyImportantLogs()
            .applyIoSchedulers()
            .subscribe({
                hasAlert.value = it
            }, {
                hasAlert.value = true
                Timber.e(it, "Log monitor")
                ELog.submitCurrentLogSilently("Log monitor", false)
            }).addTo(this.disposable)
    }
}