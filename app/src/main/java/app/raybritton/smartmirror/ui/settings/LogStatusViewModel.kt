package app.raybritton.smartmirror.ui.settings

import androidx.lifecycle.MutableLiveData
import app.raybritton.elog.ELog
import app.raybritton.smartmirror.arch.MirrorModule
import app.raybritton.smartmirror.arch.PrefModule
import app.raybritton.smartmirror.data.models.Event
import app.raybritton.smartmirror.data.monitors.LogReader
import app.raybritton.smartmirror.ext.applyIoSchedulers
import app.raybritton.smartmirror.ui.arch.BaseViewModel
import com.f2prateek.rx.preferences2.Preference
import io.reactivex.BackpressureStrategy
import io.reactivex.rxkotlin.addTo
import org.joda.time.DateTime
import timber.log.Timber

class LogStatusViewModel(
    private val logReader: LogReader = MirrorModule.logReader,
    private val unreadImportantEventId: Preference<Long> = PrefModule.latestUnreadImportantEventId
) : BaseViewModel() {
    val logs = MutableLiveData<Result>()
    val unreadEventId = MutableLiveData<Long>(PrefModule.NO_EVENT)

    init {
        load(DateTime.now())
    }

    fun load(day: DateTime) {
        unreadImportantEventId
            .asObservable()
            .toFlowable(BackpressureStrategy.LATEST)
            .applyIoSchedulers()
            .subscribe({
                unreadEventId.value = it
            }, {
                Timber.e(it, "Loading unread event id")
                ELog.submitCurrentLogSilently("Loading unread event id", false)
            })
            .addTo(this.disposable)
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

    fun latestEventRead() {
        unreadImportantEventId.set(PrefModule.NO_EVENT)
    }
}

sealed class Result(val dateTime: DateTime) {
    class Success(dateTime: DateTime, val logs: List<Event>) : Result(dateTime)
    class Error(dateTime: DateTime, val message: String) : Result(dateTime)
}