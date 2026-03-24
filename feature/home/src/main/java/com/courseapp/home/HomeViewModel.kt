package com.courseapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courseapp.domain.model.Course
import com.courseapp.domain.usecase.GetCoursesUseCase
import com.courseapp.domain.usecase.SortCoursesByPublishDateUseCase
import com.courseapp.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeState(
    val courses: List<Course> = emptyList(),
    val sortByDateDesc: Boolean = true,
    val loading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val sortCoursesByPublishDateUseCase: SortCoursesByPublishDateUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadCourses()
    }

    fun loadCourses() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            getCoursesUseCase()
                .onSuccess { list ->
                    _state.value = _state.value.copy(
                        courses = sortByPublishDate(list, desc = true),
                        loading = false
                    )
                }
                .onFailure {
                    _state.value = _state.value.copy(
                        loading = false,
                        error = it.message
                    )
                }
        }
    }

    fun toggleSort() {
        val desc = !_state.value.sortByDateDesc
        _state.value = _state.value.copy(
            courses = sortByPublishDate(_state.value.courses, desc),
            sortByDateDesc = desc
        )
    }

    private fun sortByPublishDate(items: List<Course>, desc: Boolean): List<Course> {
        return sortCoursesByPublishDateUseCase(items, desc)
    }

    fun toggleFavorite(item: Course) {
        viewModelScope.launch {
            val updatedCourse = toggleFavoriteUseCase(item)
            _state.value = _state.value.copy(
                courses = _state.value.courses.map {
                    if (it.id == updatedCourse.id) updatedCourse
                    else it
                }
            )
        }
    }
}
