package com.dicoding.intermediate.di.features

import com.dicoding.intermediate.ui.vm.AddStoryVM
import com.dicoding.intermediate.ui.vm.DetailStoryVM
import com.dicoding.intermediate.ui.vm.HomeVM
import com.dicoding.intermediate.ui.vm.LoginVM
import com.dicoding.intermediate.ui.vm.RegisterVM
import org.koin.dsl.module

val viewModelModule = module {
    single { RegisterVM(get()) }
    single { LoginVM(get()) }
    single { HomeVM(get()) }
    single { AddStoryVM(get()) }
    single { DetailStoryVM(get()) }
}