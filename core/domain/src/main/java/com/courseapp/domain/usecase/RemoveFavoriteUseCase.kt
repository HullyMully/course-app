package com.courseapp.domain.usecase

import com.courseapp.domain.repository.CoursesRepository

class RemoveFavoriteUseCase(
    private val repository: CoursesRepository
) {
    suspend operator fun invoke(courseId: String) {
        repository.removeFromFavorites(courseId)
    }
}
