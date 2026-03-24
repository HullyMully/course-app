package com.courseapp.domain.di

import com.courseapp.domain.usecase.GetCoursesUseCase
import com.courseapp.domain.usecase.GetFavoritesUseCase
import com.courseapp.domain.usecase.RemoveFavoriteUseCase
import com.courseapp.domain.usecase.SortCoursesByPublishDateUseCase
import com.courseapp.domain.usecase.ToggleFavoriteUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetCoursesUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }
    factory { GetFavoritesUseCase(get()) }
    factory { RemoveFavoriteUseCase(get()) }
    factory { SortCoursesByPublishDateUseCase() }
}
