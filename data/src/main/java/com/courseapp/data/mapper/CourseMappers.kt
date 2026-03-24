package com.courseapp.data.mapper

import com.courseapp.data.api.CourseDto
import com.courseapp.data.db.CourseEntity
import com.courseapp.domain.model.Course

fun CourseDto.toDomain(isFavorite: Boolean): Course = Course(
    id = id,
    title = title,
    description = text,
    price = price,
    rate = rate,
    startDate = startDate,
    publishDate = publishDate,
    imageUrl = imageUrl,
    isFavorite = isFavorite
)

fun CourseEntity.toDomain(): Course = Course(
    id = id,
    title = title,
    description = text,
    price = price,
    rate = rate,
    startDate = startDate,
    publishDate = publishDate,
    imageUrl = imageUrl,
    isFavorite = true
)

fun Course.toEntity(): CourseEntity = CourseEntity(
    id = id,
    title = title,
    text = description,
    price = price,
    rate = rate,
    startDate = startDate,
    publishDate = publishDate,
    imageUrl = imageUrl
)
