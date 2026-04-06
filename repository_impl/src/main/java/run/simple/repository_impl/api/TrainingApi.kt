package run.simple.repository_impl.api

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import run.simple.repository_api.data.TrainingResponse

interface TrainingApi {

    @Headers(
        "App-Token: test-app-token",
        "Authorization: Bearer test-token"
    )
    @GET("interval-timers/{id}")
    suspend fun fetchTraining(@Path("id") id: Int): TrainingResponse
}
