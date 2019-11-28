package app.raybritton.smartmirror.data.templates

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import app.raybritton.smartmirror.BuildConfig
import app.raybritton.smartmirror.R
import app.raybritton.smartmirror.data.models.*
import org.joda.time.DateTime
import java.util.*
import kotlin.math.roundToInt

data class WeatherTemplate(
    val currently: WeatherDetail,
    val minutely: Minutely,
    val hourly: Hourly,
    val daily: Daily
) {
    fun toWeather(): Weather {
        return Weather(
            currently.toCurrent(),
            minutely.toNextHour(),
            hourly.today(),
            hourly.tomorrow(daily.tomorrowSummary())
        )
    }
}

data class Daily(
    val data: List<DailyWeather>
) {
    fun tomorrowSummary(): String {
        val day = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.get(Calendar.DAY_OF_YEAR)
        return data.first {
            Calendar.getInstance().apply {
                timeInMillis = it.time * 1000L
            }.get(Calendar.DAY_OF_YEAR) == day
        }.summary
    }
}

data class DailyWeather(
    val time: Long,
    val summary: String
)

data class Hourly(
    val summary: String,
    val data: List<WeatherDetail>
) {

    fun today(): Day {
        val day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val todaysData = data.filter {
            Calendar.getInstance().apply {
                timeInMillis = it.time * 1000L
            }.get(Calendar.DAY_OF_YEAR) == day
        }
        return makeDay(summary, todaysData)
    }

    fun tomorrow(summary: String): Day {
        val day =
            Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.get(Calendar.DAY_OF_YEAR)
        val todaysData = data.filter {
            Calendar.getInstance().apply {
                timeInMillis = it.time * 1000L
            }.get(Calendar.DAY_OF_YEAR) == day
        }
        return makeDay(summary, todaysData)
    }

    private fun makeDay(summary: String, weatherData: List<WeatherDetail>): Day {
        val data = weatherData.filter {
            val hour = DateTime(it.time * 1000L).hourOfDay
            hour >= BuildConfig.CORE_HOURS_START && hour <= BuildConfig.CORE_HOURS_END
        }
        val worstWeather = data.maxBy { getPriority(it.icon) }!!
        val highestPrecip = data.maxBy { it.precipIntensity }?.precipIntensity ?: 0.0
        return Day(
            summary,
            weatherData.minBy { it.temperature }!!.temperature.toInt(),
            weatherData.maxBy { it.temperature }!!.temperature.toInt(),
            weatherData.map { it.windSpeed }.max()!!.toInt(),
            weatherData.any { it.precipIntensity > 0 },
            Precip.create(worstWeather.icon),
            getIcon(worstWeather.icon, highestPrecip)
        )
    }
}

data class Minutely(
    val summary: String,
    val data: List<RainDetails>
) {
    fun toNextHour(): NextHour {
        val isPrecip = data.any { it.precipProbability > 0 && it.precipIntensity >= BuildConfig.MIN_VALID_PRECIP }
        val highestPrecip = data.maxBy { it.precipIntensity }?.precipIntensity ?: 0.0
        return NextHour(
            isPrecip,
            data.firstOrNull { it.precipIntensity >= BuildConfig.MIN_VALID_PRECIP && it.precipProbability > 0 }
                ?.precipType.let {
                Precip.create(it)
            },
            data.indexOfFirst { it.precipProbability > 0 && it.precipIntensity >= BuildConfig.MIN_VALID_PRECIP },
            getIcon(if (isPrecip) "rain" else "clear-day", highestPrecip)
        )
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
    fun toCurrent(): Current {
        return Current(
            temperature.roundToInt(),
            windSpeed.roundToInt(),
            getIcon(icon, precipIntensity)
        )
    }
}

@DrawableRes
private fun getIcon(icon: String, precipIntensity: Double): Int {
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

private fun getPriority(icon: String): Int {
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