package com.courseapp.domain.usecase

import com.courseapp.domain.model.Course
import com.courseapp.domain.repository.CoursesRepository
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase(
    private val repository: CoursesRepository
) {
    operator fun invoke(): Flow<List<Course>> = repository.observeFavoriteCourses()
}
