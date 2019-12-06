package app.raybritton.smartmirror.data.api

import app.raybritton.smartmirror.data.templates.IpTemplate
import io.reactivex.Single
import retrofit2.http.GET

interface IpService {
    @GET("ip")
    fun getIpAddress(): Single<IpTemplate>
}