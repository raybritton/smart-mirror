package app.raybritton.smartmirror.data.api

import app.raybritton.smartmirror.BuildConfig
import app.raybritton.smartmirror.data.templates.WeatherDiff
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherDiffService {
    @GET(BuildConfig.WEATHER_DIFF_URL)
    fun getWeatherDiff(@Query("key") key: String = BuildConfig.WEATHER_DIFF_KEY): Single<WeatherDiff>
}