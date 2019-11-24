package app.raybritton.smartmirror

import android.app.Application
import android.graphics.Color
import app.raybritton.elog.ELog
import app.raybritton.elog.ELogConfig
import app.raybritton.elog.ELogIdGen
import app.raybritton.smartmirror.arch.MirrorModule
import app.raybritton.smartmirror.arch.PrefModule
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber

class MirrorApp : Application() {
    override fun onCreate() {
        MirrorModule.application = this
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        ELog.appPackage = this.packageName
        ELog.appBuild = "${BuildConfig.BUILD_TYPE} ${BuildConfig.FLAVOR}"
        ELog.appVersion = BuildConfig.VERSION_CODE
        ELog.appVersionName = BuildConfig.VERSION_NAME
        ELog.contactName = ""
        ELog.contactEmail = ""
        ELog.deviceId = if (BuildConfig.DEBUG) "Test device" else "SmartMirror"
        ELog.idAutoGen = ELogIdGen.NUMBER
        ELogConfig.toolbarBackgroundColor = Color.BLACK
        ELog.plant(this, "https://elogs.dokku-ray.app")

        JodaTimeAndroid.init(this)

        MirrorModule.monitorManager.start()

        PrefModule.updateAvailable.set(false)
    }
}