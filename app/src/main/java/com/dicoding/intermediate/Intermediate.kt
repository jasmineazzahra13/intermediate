package com.dicoding.intermediate

import android.app.Application
import com.dicoding.intermediate.di.features.authModule
import com.dicoding.intermediate.di.features.storyModule
import com.dicoding.intermediate.di.features.viewModelModule
import com.dicoding.intermediate.di.localModule
import com.dicoding.intermediate.di.networkModule
import com.dicoding.intermediate.di.preferModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class Intermediate : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@Intermediate)
            modules(
                listOf(
                    networkModule,
                    localModule,
                    viewModelModule,
                    storyModule,
                    authModule,
                    preferModule
                )
            )
        }
    }
}