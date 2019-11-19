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

            weather_current_icon.setImageResource(weather.now.icon)
            if (weather.soon.isPrecip && weather.now.windSpeed > 15) {
                weather_current_temperature.text = String.format(
                    "%d°C,  %d mph",
                    weather.now.temperature,
                    Util.kmphToMph(weather.now.windSpeed)
                )
            } else {
                weather_current_temperature.text = String.format("%d°C", weather.now.temperature)
            }

            if (weather.soon.isPrecip) {
                val minsToPrecip = weather.soon.timeToPrecip
                weather_current_summary.visibility = View.VISIBLE
                if (minsToPrecip < 5) {
                    weather_current_summary.text = context.getString(
                        R.string.precip_now,
                        context.getString(weather.soon.precipType.current)
                    )
                } else {
                    weather_current_summary.text = context.getString(
                        R.string.precip_future,
                        context.getString(weather.soon.precipType.future),
                        minsToPrecip
                    )
                }
            } else {
                weather_current_summary.visibility = View.GONE
            }

            weather_today_icon.setImageResource(weather.today.icon)
            if (weather.today.isPrecip && weather.today.windSpeed > 15) {
                weather_today_temperature.text = String.format(
                    "%d - %d°C,  %d mph",
                    weather.today.minTemp,
                    weather.today.maxTemp,
                    Util.kmphToMph(weather.today.windSpeed)
                )
                weather_summary.visibility = View.VISIBLE
                weather_summary.text = weather.today.summary
            } else {
                weather_summary.visibility = View.GONE
                weather_today_temperature.text =
                    String.format("%d - %d°C", weather.today.minTemp, weather.today.maxTemp)
            }


            weather_tomorrow_icon.setImageResource(weather.tomorrow.icon)
            if (weather.tomorrow.isPrecip && weather.tomorrow.windSpeed > 15) {
                weather_tomorrow_temperature.text = String.format(
                    "%d - %d°C,  %d mph",
                    weather.tomorrow.minTemp,
                    weather.tomorrow.maxTemp,
                    Util.kmphToMph(weather.tomorrow.windSpeed)
                )
            } else {
                weather_tomorrow_temperature.text =
                    String.format("%d - %d°C", weather.tomorrow.minTemp, weather.tomorrow.maxTemp)
            }

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