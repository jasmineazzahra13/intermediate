package com.dicoding.intermediate.di.features

import com.dicoding.intermediate.data.repo.AuthRepo
import com.dicoding.intermediate.data.resource.AuthDS
import org.koin.dsl.module

val authModule = module {
    single { AuthRepo(get()) }
    single { AuthDS(get(), get())}
}