package app.raybritton.smartmirror.ui.mirror

import androidx.lifecycle.MutableLiveData
import app.raybritton.elog.ELog
import app.raybritton.smartmirror.arch.MirrorModule
import app.raybritton.smartmirror.data.WeatherManager
import app.raybritton.smartmirror.ext.applyIoSchedulers
import app.raybritton.smartmirror.ui.arch.BaseViewModel
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class MirrorViewModel(weatherManager: WeatherManager = MirrorModule.weatherManager) : BaseViewModel() {

    val weatherData = MutableLiveData<Boolean>(false)

    init {
        weatherManager.watchSystemFailure()
            .applyIoSchedulers()
            .subscribe({
                weatherData.value = true
            },{
                Timber.e(it, "System failure failed")
                ELog.submitCurrentLogSilently("System failure failed", false)
                weatherData.value = true
            }).addTo(this.disposable)
    }
}