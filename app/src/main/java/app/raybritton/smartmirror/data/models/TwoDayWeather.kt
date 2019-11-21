package app.raybritton.smartmirror.data.models

data class TwoDayWeather(
    val today: List<WeatherPoint>,
    val yesterday: List<WeatherPoint>
) {
    fun minTemp(): Int {
        return today.minBy { it.temp }!!.temp.coerceAtMost(yesterday.minBy { it.temp }!!.temp)
    }

    fun maxTemp(): Int {
        return today.maxBy { it.temp }!!.temp.coerceAtLeast(yesterday.maxBy { it.temp }!!.temp)
    }
}

data class WeatherPoint(
    val temp: Int,
    val raining: Boolean
)