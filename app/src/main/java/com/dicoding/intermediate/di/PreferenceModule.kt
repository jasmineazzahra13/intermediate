package com.dicoding.intermediate.di


import com.dicoding.intermediate.utils.PreferManager
import org.koin.dsl.module

val preferModule = module {

    single { PreferManager(get()) }
}