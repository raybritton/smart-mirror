package app.raybritton.smartmirror.ui.mirror

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import app.raybritton.smartmirror.R
import app.raybritton.smartmirror.data.Util
import app.raybritton.smartmirror.data.WeatherManager
import kotlinx.android.synthetic.main.view_weather.view.*

class WeatherView : FrameLayout {
    private val manager by lazy { WeatherManager() }

    private var lastUpdate = 0L

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.view_weather, this)
    }

    fun update() {
        if (lastUpdate + UPDATE_LIMIT_MS > System.currentTimeMillis()) return
        lastUpdate = System.currentTimeMillis()
        manager.updateWeather({ weather ->
            setVisibility(
                View.VISIBLE,
                weather_current_icon,
                weather_current_temperature,
                weather_summary,
                weather_today_icon,
                weather_today_temperature
            )
            weather_error.visibility = View.GONE

            weather_current_icon.setImageResource(weather.currently.getIcon())
            if ((weather.currently.isPercip() || weather.minutely.hasPrecip()) && weather.currently.windSpeed > 15) {
                weather_current_temperature.text = String.format(
                    "%.0f°C,  %.0f mph",
                    weather.currently.temperature,
                    Util.kmphToMph(weather.currently.windSpeed)
                )
            } else {
                weather_current_temperature.text = String.format("%.0f°C", weather.currently.temperature)
            }

            if (weather.minutely.hasPrecip()) {
                val minsToPrecip = weather.minutely.timeToPrecip()

                if (minsToPrecip < 5) {
                    weather_current_summary.text = context.getString(
                        R.string.precip_now,
                        context.getString(weather.minutely.precipType()!!.current)
                    )
                } else {
                    weather_current_summary.text = context.getString(
                        R.string.precip_future,
                        context.getString(weather.minutely.precipType()!!.future),
                        minsToPrecip
                    )
                }
            }

            weather_today_icon.setImageResource(weather.hourly.getIcon())
            if (weather.hourly.hasPrecip() && weather.hourly.windSpeed() > 15) {
                weather_today_temperature.text = String.format(
                    "%.0f - %.0f°C,  %.0f mph",
                    weather.hourly.minTemp(),
                    weather.hourly.maxTemp(),
                    Util.kmphToMph(weather.hourly.windSpeed())
                )
            } else {
                weather_today_temperature.text =
                    String.format("%.0f - %.0f°C", weather.hourly.minTemp(), weather.hourly.maxTemp())
            }
            weather_summary.text = weather.hourly.summary

        }, { error ->
            setVisibility(
                View.GONE,
                weather_current_icon,
                weather_current_temperature,
                weather_summary,
                weather_today_icon,
                weather_today_temperature
            )
            weather_error.visibility = View.VISIBLE
            weather_error.text = error
        })
    }

    fun setVisibility(value: Int, vararg views: View) {
        views.forEach { it.visibility = value }
    }

    companion object {
        private const val UPDATE_LIMIT_MS = 5 * 60 * 1000L //5 mins
    }
}