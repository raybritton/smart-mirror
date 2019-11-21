package app.raybritton.smartmirror.data.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import app.raybritton.smartmirror.R

data class Weather(
    val now: Current,
    val soon: NextHour,
    val today: Day,
    val tomorrow: Day
)

data class Current(
    val temperature: Int,
    val windSpeed: Int,
    @DrawableRes val icon: Int
)

data class NextHour(
    val isPrecip: Boolean,
    val precipType: Precip,
    val timeToPrecip: Int,
    @DrawableRes val icon: Int
)

data class Day(
    val summary: String,
    val minTemp: Int,
    val maxTemp: Int,
    val windSpeed: Int,
    val isPrecip: Boolean,
    val precipType: Precip?,
    @DrawableRes val icon: Int
)


enum class Precip(@StringRes val future: Int, @StringRes val current: Int) {
    RAIN(R.string.precip_rain, R.string.precip_rain_now),
    SNOW(R.string.precip_snow, R.string.precip_snow_now),
    HAIL(R.string.precip_hail, R.string.precip_hail_now),
    UNKNOWN(R.string.precip_unknown, R.string.precip_unknown_now);

    companion object {
        fun create(code: String?): Precip {
            return when (code) {
                "rain" -> RAIN
                "snow" -> SNOW
                "hail", "sleet" -> HAIL
                else -> UNKNOWN
            }
        }
    }
}