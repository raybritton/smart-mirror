package app.raybritton.smartmirror.data.monitors

import android.annotation.SuppressLint
import android.app.Application
import app.raybritton.elog.ELog
import app.raybritton.smartmirror.arch.PrefModule
import app.raybritton.smartmirror.ext.applyIoSchedulers
import com.f2prateek.rx.preferences2.Preference
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import io.reactivex.Flowable
import timber.log.Timber
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class UpdateMonitorImpl(
    app: Application,
    private val updateAvailable: Preference<Boolean> = PrefModule.updateAvailable
) : UpdateMonitor {
    private val updateFactory = AppUpdateManagerFactory.create(app)

    override fun monitor() {
        Flowable.interval(0L, 5, TimeUnit.MINUTES)
            .applyIoSchedulers()
            .subscribe {
                updateFactory.appUpdateInfo
                    .addOnSuccessListener {
                        updateAvailable.set(it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                    }
                    .addOnFailureListener {
                        Timber.e(it, "Failed to get update availability")
                        ELog.submitCurrentLogSilently("Update availability", false)
                    }
            }
    }
}