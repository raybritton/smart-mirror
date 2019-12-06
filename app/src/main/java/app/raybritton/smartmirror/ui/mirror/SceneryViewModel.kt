package app.raybritton.smartmirror.ui.mirror

import androidx.lifecycle.MutableLiveData
import app.raybritton.elog.ELog
import app.raybritton.smartmirror.R
import app.raybritton.smartmirror.arch.MirrorModule
import app.raybritton.smartmirror.data.WeatherManager
import app.raybritton.smartmirror.ext.applyIoSchedulers
import app.raybritton.smartmirror.ui.arch.BaseViewModel
import io.reactivex.Flowable
import io.reactivex.rxkotlin.addTo
import org.joda.time.DateTime
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SceneryViewModel(
    weatherManager: WeatherManager = MirrorModule.weatherManager
) : BaseViewModel() {
    val dayOfChristmas = MutableLiveData<Int?>(null)
    val mode = MutableLiveData<SceneryView.Mode>(SceneryView.Mode.NOTHING)

    init {
        Flowable.interval(30, TimeUnit.SECONDS)
            .applyIoSchedulers()
            .subscribe({
                val now = DateTime.now()
                if (now.monthOfYear == 12) {
                    if (now.dayOfMonth >= 25) {
                        dayOfChristmas.value = 0
                    } else {
                        dayOfChristmas.value = 25 - now.dayOfMonth
                    }
                } else {
                    dayOfChristmas.value = null
                }
            },{
                Timber.e(it, "christmas monitor")
                ELog.submitCurrentLogSilently("christmas monitor", false)
            })
            .addTo(this.disposable)

        weatherManager.watchForecast()
            .map {
                when (it.getOrNull()?.soon?.icon) {
                    R.drawable.ic_light_rain, R.drawable.ic_rain -> SceneryView.Mode.RAIN
                    R.drawable.ic_heavy_rain -> SceneryView.Mode.HEAVY_RAIN
                    R.drawable.ic_snow -> SceneryView.Mode.SNOW
                    R.drawable.ic_hail -> SceneryView.Mode.HEAVY_RAIN
                    R.drawable.ic_thunder -> SceneryView.Mode.THUNDER
                    else -> SceneryView.Mode.NOTHING
                }
            }
            .applyIoSchedulers()
            .subscribe({
                mode.value = it
            },{
                Timber.e(it, "scenery weather")
                ELog.submitCurrentLogSilently("scenery weather", false)
            })
            .addTo(this.disposable)
    }
}