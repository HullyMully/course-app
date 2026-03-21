package com.courseapp

import android.app.Application
import com.courseapp.account.di.accountModule
import com.courseapp.favorites.di.favoritesModule
import com.courseapp.home.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CourseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CourseApp)
            modules(com.courseapp.data.di.dataModule, homeModule, favoritesModule, accountModule)
        }
    }
}
