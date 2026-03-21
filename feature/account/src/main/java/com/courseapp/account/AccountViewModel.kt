package com.courseapp.account

import androidx.lifecycle.ViewModel
import com.courseapp.data.db.FavoriteEntity
import com.courseapp.data.repository.CoursesRepository
import kotlinx.coroutines.flow.Flow

class AccountViewModel(
    private val repository: CoursesRepository
) : ViewModel() {
    val courses: Flow<List<FavoriteEntity>> = repository.getFavorites()
}
