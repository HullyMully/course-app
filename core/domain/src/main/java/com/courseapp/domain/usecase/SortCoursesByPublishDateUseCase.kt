package com.courseapp.domain.usecase

import com.courseapp.domain.model.Course

class SortCoursesByPublishDateUseCase {
    operator fun invoke(courses: List<Course>, desc: Boolean): List<Course> {
        return if (desc) courses.sortedByDescending { it.publishDate } else courses.sortedBy { it.publishDate }
    }
}
