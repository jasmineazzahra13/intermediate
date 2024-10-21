package com.dicoding.intermediate.di

import com.dicoding.intermediate.BuildConfig
import com.dicoding.intermediate.data.network.HeaderInterceptor
import com.dicoding.intermediate.data.network.services.AuthServ
import com.dicoding.intermediate.data.network.services.StoryServ
import com.dicoding.intermediate.utils.PreferManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        return@single OkHttpClient.Builder()
            .addInterceptor(getHeaderInterceptor(get()))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single { provideAuthService(get()) }
    single { provideStoryService(get()) }
}

private val loggingInterceptor = if (BuildConfig.DEBUG) {
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
} else {
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
}

private fun getHeaderInterceptor(preferManager: PreferManager): Interceptor {
    val headers = HashMap<String, String>()
    headers["Content-Type"] = "application/json"
    return HeaderInterceptor(headers, preferManager)
}

fun provideAuthService(retrofit: Retrofit): AuthServ =
    retrofit.create(AuthServ::class.java)

fun provideStoryService(retrofit: Retrofit): StoryServ =
    retrofit.create(StoryServ::class.java)