package com.dicoding.intermediate.di.features

import com.dicoding.intermediate.data.repo.StoryRepo
import com.dicoding.intermediate.data.resource.StoryDS
import org.koin.dsl.module

val storyModule = module {
    single { StoryRepo(get()) }
    single { StoryDS(get(), get()) }
}