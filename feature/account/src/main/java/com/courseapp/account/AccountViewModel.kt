package com.courseapp.account

import androidx.lifecycle.ViewModel
import com.courseapp.domain.model.Course
import com.courseapp.domain.usecase.GetFavoritesUseCase
import kotlinx.coroutines.flow.Flow

class AccountViewModel(
    getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {
    val courses: Flow<List<Course>> = getFavoritesUseCase()
}
