package app.raybritton.smartmirror.data

import app.raybritton.smartmirror.BuildConfig
import app.raybritton.smartmirror.arch.MirrorModule
import app.raybritton.smartmirror.data.api.DarkSkyService
import app.raybritton.smartmirror.data.models.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class WeatherManager(private val service: DarkSkyService = MirrorModule.darkSkyService) {
    fun updateWeather(callback: (Weather) -> Unit, error: (String) -> Unit) {
        service.getWeather(BuildConfig.DARKSKY_KEY, BuildConfig.LATLNG)
            .enqueue(object: Callback<Weather> {
                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    Timber.e(t, "Updating weather")
                    error("Error updating weather: ${t.message ?: t.javaClass.simpleName}")
                }

                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                    if (response.isSuccessful) {
                        callback(response.body()!!)
                    } else {
                        error("${response.code()} ${response.message()}")
                    }
                }
            })
    }
}