package app.raybritton.smartmirror.data.templates

import app.raybritton.smartmirror.BuildConfig
import app.raybritton.smartmirror.data.models.TwoDayWeather
import app.raybritton.smartmirror.data.models.WeatherPoint

data class WeatherDiff(
    val yesterday: List<HourDiff>,
    val today: TodayDiff
) {
    fun toTwoDayWeather(): TwoDayWeather {
        return TwoDayWeather(
            today.toWeatherPoints(),
            yesterday.map { it.toWeatherPoint() }
        )
    }
}

data class TodayDiff(
    val history: List<HourDiff>,
    val prediction: List<HourDiff>
) {
    fun toWeatherPoints(): List<WeatherPoint> {
        return history.map { it.toWeatherPoint() } + prediction.drop(1).map { it.toWeatherPoint() }
    }
}

data class HourDiff(
    val hour: Int,
    val temp: Int,
    val icon: String,
    val rainIntensity: Double,
    val windSpeed: Int
) {
    fun toWeatherPoint(): WeatherPoint {
        return WeatherPoint(
            temp,
            rainIntensity > BuildConfig.MIN_VALID_PRECIP
        )
    }
}