package com.courseapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CourseEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}

fun createDatabase(context: Context): AppDatabase = Room
    .databaseBuilder(context, AppDatabase::class.java, "course_app_db")
    // .fallbackToDestructiveMigration() // TODO: Replace with manual migration before release.
    .build()
