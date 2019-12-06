package app.raybritton.smartmirror.data.api

import app.raybritton.smartmirror.data.templates.FirewallTemplate
import io.reactivex.Completable
import retrofit2.http.*

interface FirewallService {
    @POST("v2/firewalls/{id}/rules")
    fun updateFirewall(@Path("id") id: String, @Header("Authorization") token: String, @Body body: FirewallTemplate): Completable
}