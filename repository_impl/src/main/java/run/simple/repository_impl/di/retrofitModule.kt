package run.simple.repository_impl.di

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import run.simple.repository_impl.api.TrainingApi
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://71-cl5.tz.testing.place/api/"
private const val timeout = 30L

internal val retrofitModule = module {
    single<OkHttpClient> {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.connectTimeout(timeout, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(timeout, TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(timeout, TimeUnit.SECONDS)
        okHttpClientBuilder.build()
    }

    single {
        val json = Json { ignoreUnknownKeys = true }
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single { get<Retrofit>().create(TrainingApi::class.java) }
}