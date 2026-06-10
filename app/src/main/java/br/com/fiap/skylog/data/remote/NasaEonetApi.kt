package br.com.fiap.skylog.data.remote

import br.com.fiap.skylog.data.remote.model.EonetResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaEonetApi {
    @GET("events")
    suspend fun getEvents(
        @Query("status") status: String = "open",
        @Query("days") days: Int = 30,
        @Query("limit") limit: Int = 50
    ): EonetResponse
}
