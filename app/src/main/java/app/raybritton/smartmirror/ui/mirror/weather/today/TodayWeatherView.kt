package app.raybritton.smartmirror.ui.mirror.weather.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.raybritton.smartmirror.BuildConfig
import app.raybritton.smartmirror.R
import app.raybritton.smartmirror.data.Util
import app.raybritton.smartmirror.data.models.Day
import app.raybritton.smartmirror.ui.arch.BaseFragment
import app.raybritton.smartmirror.ui.mirror.weather.WeatherStatus
import kotlinx.android.synthetic.main.fragment_weather.*

class TodayWeatherView : BaseFragment<TodayWeatherViewModel>(TodayWeatherViewModel::class.java) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weather_label.setText(R.string.section_today)

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
                    show(weather_icon, weather_temperature, weather_summary)
                    hide(weather_loading, weather_error)
                    setupUi(weatherStatus.weather.today)
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

    private fun setupUi(day: Day) {
        weather_temperature.text = formatTemperature(day)
        weather_summary.text = day.summary
        weather_icon.setImageResource(day.icon)
    }

    private fun formatTemperature(day: Day): String {
        return if (day.isPrecip && day.windSpeed >= BuildConfig.MIN_VALID_WIND_SPEED) {
            String.format("%d - %d°C,  %d mph", day.minTemp, day.maxTemp, Util.kmphToMph(day.windSpeed))
        } else {
            String.format("%d - %d°C", day.minTemp, day.maxTemp)
        }
    }
}
