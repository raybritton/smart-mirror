package app.raybritton.smartmirror.ui.mirror

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.BatteryManager
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import app.raybritton.smartmirror.R
import kotlinx.android.synthetic.main.view_battery.view.*

class BatteryView : FrameLayout {
    private val receiver by lazy { PowerReceiver(this::update) }
    private var animated = false
    private var animationState = 0
    private var animationSpeed = 200
    private var animationRunnable = Runnable { animationUpdate() }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.view_battery, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        context.unregisterReceiver(receiver)
    }

    private fun update(isCharging: Boolean, chargeLevel: Float) {
        val charge = (chargeLevel * 100).toInt()
        if (isCharging) {
            animated = false
            battery_text.visibility = View.GONE
            battery_icon.visibility = View.GONE
        } else {
            battery_text.visibility = View.VISIBLE
            battery_icon.visibility = View.VISIBLE

            if (chargeLevel <= 0.1) {
                animationSpeed = 100
                battery_text.text = context.getString(R.string.battery_critical, charge)
                animated = true
                animationUpdate()
            } else if (chargeLevel <= 0.2) {
                animationSpeed = 200
                battery_text.text = context.getString(R.string.battery_critical, charge)
                animated = true
                animationUpdate()
            } else {
                battery_text.text = context.getString(R.string.battery_discharging, charge)
                animated = false
            }
        }
    }

    private fun animationUpdate() {
        if (!animated) {
            battery_text.setTextColor(Color.RED)
            battery_icon.imageTintList = ColorStateList.valueOf(Color.RED)
            battery_container.setBackgroundColor(Color.BLACK)
            return
        }

        when (animationState) {
            0 -> {
                battery_text.setTextColor(Color.BLACK)
                battery_icon.imageTintList = ColorStateList.valueOf(Color.BLACK)
                battery_container.setBackgroundColor(Color.RED)
                animationState = 1
            }
            1 -> {
                battery_text.setTextColor(Color.RED)
                battery_icon.imageTintList = ColorStateList.valueOf(Color.RED)
                battery_container.setBackgroundColor(Color.BLACK)
                animationState = 0
            }
            else -> animationState = 0
        }

        removeCallbacks(animationRunnable)
        postDelayed(animationRunnable, 100)
    }

    private class PowerReceiver(private val callback: (Boolean, Float) -> Unit) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val isCharging = intent.getIntExtra(
                BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_UNKNOWN
            ) == BatteryManager.BATTERY_STATUS_CHARGING

            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 1).toFloat()
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1).toFloat()

            val chargeLevel = level / scale

            callback(isCharging, chargeLevel)
        }
    }
}