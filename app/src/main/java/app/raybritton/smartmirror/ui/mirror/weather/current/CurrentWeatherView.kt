package app.raybritton.smartmirror.ui.mirror.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.raybritton.smartmirror.BuildConfig
import app.raybritton.smartmirror.R
import app.raybritton.smartmirror.data.Util
import app.raybritton.smartmirror.data.models.Current
import app.raybritton.smartmirror.data.models.NextHour
import app.raybritton.smartmirror.ui.arch.BaseFragment
import app.raybritton.smartmirror.ui.mirror.weather.WeatherStatus
import kotlinx.android.synthetic.main.fragment_weather.*

class CurrentWeatherView : BaseFragment<CurrentWeatherViewModel>(CurrentWeatherViewModel::class.java) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weather_label.setText(R.string.section_now)

        viewModel.weatherData.observe { weatherStatus ->
            when (weatherStatus) {
                WeatherStatus.Loading -> {
                    hide(
                        weather_icon,
                        weather_temperature,
                        weather_error,
                        weather_summary
                    )
                    show(weather_loading)
                }
                is WeatherStatus.Success -> {
                    show(weather_icon, weather_temperature)
                    hide(weather_loading, weather_error)
                    setupUi(weatherStatus.weather.now, weatherStatus.weather.soon)
                }
                is WeatherStatus.Error -> {
                    hide(
                        weather_icon,
                        weather_temperature,
                        weather_loading,
                        weather_summary
                    )
                    show(weather_error)
                    weather_error.text = weatherStatus.text
                }
            }
        }
    }

    private fun setupUi(now: Current, soon: NextHour) {
        weather_temperature.text = formatTemperature(now, soon)
        if (soon.isPrecip) {
            show(weather_summary)
            val minsToPrecip = soon.timeToPrecip
            weather_summary.visibility = View.VISIBLE
            if (minsToPrecip < 5) {
                weather_summary.text = getString(
                    R.string.precip_now,
                    getString(soon.precipType.current)
                )
            } else {
                weather_summary.text = getString(
                    R.string.precip_future,
                    getString(soon.precipType.future),
                    minsToPrecip
                )
            }
            weather_icon.setImageResource(soon.icon)
        } else {
            hide(weather_summary)
            weather_icon.setImageResource(now.icon)
        }
    }

    private fun formatTemperature(current: Current, soon: NextHour): String {
        return if (soon.isPrecip && current.windSpeed >= BuildConfig.MIN_VALID_WIND_SPEED) {
            String.format("%d°C,  %d mph", current.temperature, Util.kmphToMph(current.windSpeed))
        } else {
            String.format("%d°C", current.temperature)
        }
    }
}
