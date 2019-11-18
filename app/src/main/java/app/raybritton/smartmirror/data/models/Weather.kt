package app.raybritton.smartmirror.data.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import app.raybritton.smartmirror.R
import java.util.*

data class Weather(
    val currently: WeatherDetail,
    val minutely: Minutely,
    val hourly: Hourly
)

data class Hourly(
    val summary: String,
    val data: List<WeatherDetail>,
    private var todayData: List<WeatherDetail>?
) {
    private fun relevantData(): List<WeatherDetail> {
        if (todayData == null) {
            val midnight = Calendar.getInstance()
            midnight.set(Calendar.HOUR_OF_DAY, 24)
            midnight.set(Calendar.MINUTE, 0)
            midnight.set(Calendar.SECOND, 0)
            todayData = data.filter { it.time <= midnight.timeInMillis}
        }
        return todayData!!
    }

    @DrawableRes
    fun getIcon(): Int {
        return relevantData().maxBy { it.getPriority() }!!.getIcon()
    }

    fun hasPrecip(): Boolean {
        return relevantData().any { it.precipIntensity > 0 }
    }

    fun minTemp(): Double {
        return relevantData().minBy { it.temperature }?.temperature ?: -1.0
    }

    fun maxTemp(): Double {
        return relevantData().maxBy { it.temperature }?.temperature ?: -1.0
    }

    fun windSpeed(): Double {
        return relevantData().map { it.windSpeed }.max() ?: -1.0
    }
}

data class Minutely(
    val summary: String,
    val data: List<RainDetails>
) {
    fun hasPrecip(): Boolean {
        return data.any { it.precipProbability > 0 && it.precipIntensity > 0 }
    }

    fun precipType(): Precip? {
        return data.firstOrNull { it.precipIntensity > 0 && it.precipProbability > 0 }
            ?.precipType?.let { Precip.create(it) }
    }

    fun timeToPrecip(): Int {
        return data.indexOfFirst { it.precipProbability > 0 && it.precipIntensity > 0 }
    }
}

enum class Precip(@StringRes val future: Int, @StringRes val current: Int) {
    RAIN(R.string.precip_rain, R.string.precip_rain_now),
    SNOW(R.string.precip_snow, R.string.precip_snow_now),
    HAIL(R.string.precip_hail, R.string.precip_hail_now),
    UNKNOWN(R.string.precip_unknown, R.string.precip_unknown_now);

    companion object {
        fun create(code: String): Precip {
            return when (code) {
                "rain" -> RAIN
                "snow" -> SNOW
                "hail", "sleet" -> HAIL
                else -> UNKNOWN
            }
        }
    }
}

data class RainDetails(
    val time: Long,
    val precipIntensity: Double,
    val precipProbability: Double,
    val precipType: String?
)

data class WeatherDetail(
    val time: Long,
    val summary: String,
    val icon: String,
    val precipIntensity: Double,
    val precipType: String?,
    val temperature: Double,
    val windSpeed: Double
) {
    fun isPercip(): Boolean {
        return precipIntensity > 0
    }

    fun precipType(): Precip? {
        return precipType?.let { Precip.valueOf(it) }
    }

    @DrawableRes
    fun getIcon(): Int {
        return when (icon) {
            "clear-day", "clear-night" -> R.drawable.ic_clear
            "sleet", "hail" -> R.drawable.ic_hail
            "cloudy" -> R.drawable.ic_cloud
            "partly-cloudy-day", "partly-cloudy-night" -> R.drawable.ic_cloud_sun
            "snow" -> R.drawable.ic_snow
            "fog" -> R.drawable.ic_fog
            "thunderstorm" -> R.drawable.ic_thunder
            "rain" -> {
                when (precipIntensity) {
                    in 0.0..0.33 -> R.drawable.ic_light_rain
                    in 0.34..0.66 -> R.drawable.ic_rain
                    in 0.67..1.0 -> R.drawable.ic_heavy_rain
                    else -> R.drawable.ic_rain
                }
            }
            else -> R.drawable.ic_weather_unknown
        }
    }

    fun getPriority(): Int {
        return when (icon) {
            "rain" -> 100
            "thunderstorm" -> 100
            "sleet", "hail" -> 100
            "snow" -> 90
            "fog" -> 20
            "cloudy" -> 10
            "partly-cloudy-day", "partly-cloudy-night" -> 10
            "clear-day", "clear-night" -> 0
            else -> -100
        }
    }
}