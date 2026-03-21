package com.courseapp.data.di

import android.content.Context
import com.courseapp.data.api.CoursesApi
import com.courseapp.data.db.AppDatabase
import com.courseapp.data.db.FavoritesDao
import com.courseapp.data.db.createDatabase
import com.courseapp.data.repository.CoursesRepository
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {
    single { createDatabase(get<Context>()) }
    single<FavoritesDao> { get<AppDatabase>().favoritesDao() }
    single {
        Retrofit.Builder()
            .baseUrl("https://drive.usercontent.google.com/")
            .client(OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<CoursesApi> { get<Retrofit>().create(CoursesApi::class.java) }
    single { CoursesRepository(get(), get()) }
}
