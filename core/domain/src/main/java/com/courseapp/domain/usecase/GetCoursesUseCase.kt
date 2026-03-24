package com.courseapp.domain.usecase

import com.courseapp.domain.model.Course
import com.courseapp.domain.repository.CoursesRepository

class GetCoursesUseCase(
    private val repository: CoursesRepository
) {
    suspend operator fun invoke(): Result<List<Course>> = repository.getCourses()
}
