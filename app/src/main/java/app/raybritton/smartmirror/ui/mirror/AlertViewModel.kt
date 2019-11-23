package app.raybritton.smartmirror.ui.mirror

import androidx.lifecycle.MutableLiveData
import app.raybritton.elog.ELog
import app.raybritton.smartmirror.arch.MirrorModule
import app.raybritton.smartmirror.arch.PrefModule
import app.raybritton.smartmirror.data.monitors.LogReader
import app.raybritton.smartmirror.ext.applyIoSchedulers
import app.raybritton.smartmirror.ui.arch.BaseViewModel
import app.raybritton.smartmirror.ui.mirror.weather.WeatherStatus
import com.f2prateek.rx.preferences2.Preference
import io.reactivex.BackpressureStrategy
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class AlertViewModel(unreadImportantEvent: Preference<Long> = PrefModule.latestUnreadImportantEventId) : BaseViewModel() {
    val hasAlert = MutableLiveData<Boolean>(false)

    init {
        unreadImportantEvent
            .asObservable()
            .toFlowable(BackpressureStrategy.LATEST)
            .applyIoSchedulers()
            .subscribe({
                hasAlert.value = it != PrefModule.NO_EVENT
            }, {
                hasAlert.value = true
                Timber.e(it, "Log monitor")
                ELog.submitCurrentLogSilently("Log monitor", false)
            }).addTo(this.disposable)
    }
}