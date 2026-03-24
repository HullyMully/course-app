package com.courseapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorites ORDER BY publishDate DESC")
    fun getAllFlow(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM favorites ORDER BY publishDate DESC")
    suspend fun getAll(): List<CourseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CourseEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun remove(id: String)

    @Query("SELECT id FROM favorites WHERE id = :id LIMIT 1")
    suspend fun isFavorite(id: String): String?

    @Query("SELECT id FROM favorites")
    suspend fun getFavoriteIds(): List<String>
}
