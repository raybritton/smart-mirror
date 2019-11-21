package app.raybritton.smartmirror.ui.mirror.weather

import app.raybritton.smartmirror.data.models.Weather

sealed class WeatherStatus {
    object Loading : WeatherStatus()
    class Success(val weather: Weather): WeatherStatus()
    class Error(val text: String): WeatherStatus()
}