package com.courseapp.data.repository

import com.courseapp.data.api.CourseDto
import com.courseapp.data.api.CoursesApi
import com.courseapp.data.db.FavoriteEntity
import com.courseapp.data.db.FavoritesDao
import kotlinx.coroutines.flow.Flow

class CoursesRepository(
    private val api: CoursesApi,
    private val favoritesDao: FavoritesDao
) {
    suspend fun loadCourses(): Result<List<CourseDto>> = runCatching {
        api.getCourses().courses
    }

    suspend fun isFavorite(id: String): Boolean = favoritesDao.isFavorite(id) != null

    suspend fun addToFavorites(dto: CourseDto) {
        favoritesDao.insert(
            FavoriteEntity(
                id = dto.id,
                title = dto.title,
                text = dto.text,
                price = dto.price,
                rate = dto.rate,
                startDate = dto.startDate,
                publishDate = dto.publishDate,
                imageUrl = dto.imageUrl
            )
        )
    }

    suspend fun removeFromFavorites(id: String) {
        favoritesDao.remove(id)
    }

    fun getFavorites(): Flow<List<FavoriteEntity>> = favoritesDao.getAllFlow()
}
