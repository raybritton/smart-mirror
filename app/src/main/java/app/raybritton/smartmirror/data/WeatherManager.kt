package app.raybritton.smartmirror.data

import app.raybritton.elog.ELog
import app.raybritton.smartmirror.BuildConfig
import app.raybritton.smartmirror.arch.MirrorModule
import app.raybritton.smartmirror.data.api.DarkSkyService
import app.raybritton.smartmirror.data.models.Weather
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class WeatherManager(private val service: DarkSkyService = MirrorModule.darkSkyService) {

    private val hasDied = ReplaySubject.createWithSize<Boolean>(1).apply { startWith(false) }
    private val forecastData = ReplaySubject.createWithSize<Result<Weather>>(1)

    init {
        Observable.interval(0L, 5, TimeUnit.MINUTES)
            .flatMap {
                service.getWeather(BuildConfig.DARKSKY_KEY, BuildConfig.LATLNG)
                    .retry(2)
                    .map { it.toWeather() }
                    .map { Result.success(it) }
                    .toObservable()
                    .onErrorReturn {
                        Timber.e(it, "Updating weather")
                        ELog.submitCurrentLogSilently("Updating weather", false)
                        Result.failure(it)
                    }
            }
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe({
                forecastData.onNext(it)
            },{
                Timber.e(it, "Updating weather - FATAL")
                ELog.submitCurrentLogSilently("Updating weather - FATAL", false)
                hasDied.onNext(true)
            })
    }

    fun watchForecast(): Flowable<Result<Weather>> {
        return forecastData.share().toFlowable(BackpressureStrategy.LATEST)
    }

    fun watchSystemFailure(): Flowable<Boolean> {
        return hasDied.share().toFlowable(BackpressureStrategy.LATEST)
    }
}