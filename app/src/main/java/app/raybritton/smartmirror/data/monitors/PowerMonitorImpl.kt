package app.raybritton.smartmirror.data.monitors

import android.app.Application
import android.content.Context
import android.os.BatteryManager
import android.os.PowerManager

class PowerMonitorImpl(private val ctx: Application,
                       private val powerLogger: PowerLogger) : PowerMonitor {
    override fun monitor() {
        val batteryManager = ctx.getSystemService(Context.BATTERY_SERVICE) as BatteryManager


    }

}