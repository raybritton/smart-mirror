package app.raybritton.smartmirror.ui.mirror.weather.tomorrow

import androidx.lifecycle.MutableLiveData
import app.raybritton.elog.ELog
import app.raybritton.smartmirror.arch.MirrorModule
import app.raybritton.smartmirror.data.WeatherManager
import app.raybritton.smartmirror.ext.applyIoSchedulers
import app.raybritton.smartmirror.ui.arch.BaseViewModel
import app.raybritton.smartmirror.ui.mirror.weather.WeatherStatus
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class TomorrowWeatherViewModel(manager: WeatherManager = MirrorModule.weatherManager) : BaseViewModel() {
    val weatherData = MutableLiveData<WeatherStatus>(
        WeatherStatus.Loading)

    init {
        manager.watchForecast()
            .applyIoSchedulers()
            .subscribe({ weather ->
                weatherData.value = when {
                    weather.isFailure -> {
                        WeatherStatus.Error(
                            weather.exceptionOrNull()?.message
                                ?: weather.exceptionOrNull()?.javaClass?.simpleName
                                ?: "Unknown error"
                        )
                    }
                    else -> {
                        WeatherStatus.Success(weather.getOrThrow())
                    }
                }
            }, {
                Timber.e(it, "Today weather")
                ELog.submitCurrentLogSilently("Today weather", false)
                weatherData.value = WeatherStatus.Error(it.message ?: it.javaClass.simpleName)
            })
            .addTo(this.disposable)
    }
}