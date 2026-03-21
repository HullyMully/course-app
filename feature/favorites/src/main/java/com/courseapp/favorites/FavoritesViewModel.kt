package com.courseapp.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courseapp.data.db.FavoriteEntity
import kotlinx.coroutines.launch
import com.courseapp.data.repository.CoursesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(
    private val repository: CoursesRepository
) : ViewModel() {

    val favorites: StateFlow<List<FavoriteEntity>> = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeFavorite(id: String) {
        viewModelScope.launch {
            repository.removeFromFavorites(id)
        }
    }
}
