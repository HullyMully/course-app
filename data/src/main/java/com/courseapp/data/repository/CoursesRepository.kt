package com.courseapp.data.repository

import com.courseapp.data.api.CoursesApi
import com.courseapp.data.db.FavoritesDao
import com.courseapp.data.mapper.toDomain
import com.courseapp.data.mapper.toEntity
import com.courseapp.domain.model.Course
import com.courseapp.domain.repository.CoursesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoursesRepositoryImpl(
    private val api: CoursesApi,
    private val favoritesDao: FavoritesDao
) : CoursesRepository {
    override suspend fun getCourses(): Result<List<Course>> = runCatching {
        val favoriteIds = favoritesDao.getFavoriteIds().toSet()
        api.getCourses().courses.map { dto ->
            dto.toDomain(isFavorite = dto.hasLike || dto.id in favoriteIds)
        }
    }

    override suspend fun addToFavorites(course: Course) {
        favoritesDao.insert(course.toEntity())
    }

    override suspend fun removeFromFavorites(courseId: String) {
        favoritesDao.remove(courseId)
    }

    override fun observeFavoriteCourses(): Flow<List<Course>> {
        return favoritesDao.getAllFlow().map { entities -> entities.map { it.toDomain() } }
    }
}
