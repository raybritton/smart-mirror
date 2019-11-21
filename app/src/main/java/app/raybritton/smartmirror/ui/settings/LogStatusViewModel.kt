package app.raybritton.smartmirror.ui.settings

import androidx.lifecycle.MutableLiveData
import app.raybritton.elog.ELog
import app.raybritton.smartmirror.arch.MirrorModule
import app.raybritton.smartmirror.data.models.Event
import app.raybritton.smartmirror.data.monitors.LogReader
import app.raybritton.smartmirror.ext.applyIoSchedulers
import app.raybritton.smartmirror.ui.arch.BaseViewModel
import io.reactivex.rxkotlin.addTo
import org.joda.time.DateTime
import timber.log.Timber

class LogStatusViewModel(private val logReader: LogReader = MirrorModule.logReader) : BaseViewModel() {
    val logs = MutableLiveData<Result>()

    init {
        load(DateTime.now())
    }

    fun load(day: DateTime) {
        logReader.getLog(day)
            .applyIoSchedulers()
            .subscribe({
                logs.value = Result.Success(day, it)
            }, {
                Timber.e(it, "Loading logs for $day")
                logs.value = Result.Error(day, it.message ?: it.javaClass.simpleName)
                ELog.submitCurrentLogSilently("Loading logs for $day", false)
            })
            .addTo(this.disposable)
    }
}

sealed class Result(val dateTime: DateTime) {
    class Success(dateTime: DateTime, val logs: List<Event>) : Result(dateTime)
    class Error(dateTime: DateTime, val message: String) : Result(dateTime)
}