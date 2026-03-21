package com.courseapp.data.api

import retrofit2.http.GET

interface CoursesApi {
    @GET("u/0/uc?id=15arTK7XT2b7Yv4BJsmDctA4Hg-BbS8-q&export=download")
    suspend fun getCourses(): CoursesResponse
}

data class CoursesResponse(
    val courses: List<CourseDto>
)

data class CourseDto(
    val id: String,
    val title: String,
    val text: String,
    val price: String,
    val rate: Double,
    val startDate: String,
    val hasLike: Boolean,
    val publishDate: String,
    val imageUrl: String? = null
)
