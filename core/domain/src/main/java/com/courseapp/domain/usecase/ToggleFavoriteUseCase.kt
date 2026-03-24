package com.courseapp.domain.usecase

import com.courseapp.domain.model.Course
import com.courseapp.domain.repository.CoursesRepository

class ToggleFavoriteUseCase(
    private val repository: CoursesRepository
) {
    suspend operator fun invoke(course: Course): Course {
        return if (course.isFavorite) {
            repository.removeFromFavorites(course.id)
            course.copy(isFavorite = false)
        } else {
            repository.addToFavorites(course)
            course.copy(isFavorite = true)
        }
    }
}
