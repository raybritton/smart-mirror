package app.raybritton.smartmirror.data.api

import app.raybritton.smartmirror.data.templates.WeatherTemplate
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DarkSkyService {
    @GET("/forecast/{darkSkyKey}/{search}?exclude=flags,alerts,daily&units=si")
    fun getWeather(@Path("darkSkyKey") apiKey: String, @Path("search") search: String): Call<WeatherTemplate>
}