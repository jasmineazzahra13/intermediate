package com.dicoding.intermediate.di

import android.app.Application
import androidx.room.Room
import com.dicoding.intermediate.BuildConfig
import com.dicoding.intermediate.data.db.StoryDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val localModule = module {
    single { provideAuthService(get()) }
    single { get<StoryDatabase>().getStoryDao() }

    fun provideDatabase(application: Application): StoryDatabase {
        return Room.databaseBuilder(application, StoryDatabase::class.java, BuildConfig.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    single {provideDatabase(androidApplication()) }
}