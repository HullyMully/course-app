package com.courseapp.core.util

import android.content.res.Resources

object ImageUtils {

    fun resolveLocalCourseImage(title: String, resources: Resources, packageName: String): Int {
        val name = when {
            title.contains("Java", ignoreCase = true) -> "course_java"
            title.contains("3D", ignoreCase = true) ||
                title.contains("дженералист", ignoreCase = true) -> "course_3d"
            else -> "course_x"
        }
        return resources.getIdentifier(name, "drawable", packageName)
    }
}
