package com.courseapp.account.di

import com.courseapp.account.AccountViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountModule = module {
    viewModel { AccountViewModel(get()) }
}
