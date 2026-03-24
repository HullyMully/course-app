package com.courseapp.domain.model

data class Course(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val rate: Double,
    val startDate: String,
    val publishDate: String,
    val imageUrl: String?,
    val isFavorite: Boolean
)
