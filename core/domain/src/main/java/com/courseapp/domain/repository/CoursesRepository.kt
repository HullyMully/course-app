package com.courseapp.domain.repository

import com.courseapp.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CoursesRepository {
    suspend fun getCourses(): Result<List<Course>>
    suspend fun addToFavorites(course: Course)
    suspend fun removeFromFavorites(courseId: String)
    fun observeFavoriteCourses(): Flow<List<Course>>
}
