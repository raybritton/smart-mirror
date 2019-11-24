package app.raybritton.smartmirror.ui.mirror

import androidx.lifecycle.MutableLiveData
import app.raybritton.elog.ELog
import app.raybritton.smartmirror.arch.PrefModule
import app.raybritton.smartmirror.ext.applyIoSchedulers
import app.raybritton.smartmirror.ui.arch.BaseViewModel
import com.f2prateek.rx.preferences2.Preference
import io.reactivex.BackpressureStrategy
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class UpdateViewModel(updateAvailablePref: Preference<Boolean> = PrefModule.updateAvailable) : BaseViewModel() {
    val updateAvailable = MutableLiveData<Boolean>(false)

    init {
        updateAvailablePref.asObservable()
            .toFlowable(BackpressureStrategy.LATEST)
            .applyIoSchedulers()
            .subscribe({
                updateAvailable.value = it
            },{
                Timber.e(it, "Update available")
                ELog.submitCurrentLogSilently("Update available", false)
            })
            .addTo(this.disposable)
    }
}