package app.raybritton.smartmirror

import android.app.Application
import app.raybritton.smartmirror.arch.MirrorModule
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber

class MirrorApp : Application() {
    override fun onCreate() {
        MirrorModule.application = this
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        JodaTimeAndroid.init(this)

        MirrorModule.monitorManager.start()
    }
}