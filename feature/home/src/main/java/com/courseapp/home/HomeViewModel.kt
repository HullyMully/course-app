package com.courseapp.home

import androidx.recyclerview.widget.DiffUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courseapp.data.api.CourseDto
import com.courseapp.data.repository.CoursesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CourseItem(
    val dto: CourseDto,
    val isFavorite: Boolean
) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<CourseItem>() {
            override fun areItemsTheSame(old: CourseItem, new: CourseItem) = old.dto.id == new.dto.id
            override fun areContentsTheSame(old: CourseItem, new: CourseItem) = old == new
        }
    }
}

data class HomeState(
    val courses: List<CourseItem> = emptyList(),
    val sortByDateDesc: Boolean = true,
    val loading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(
    private val repository: CoursesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadCourses()
    }

    fun loadCourses() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            repository.loadCourses()
                .onSuccess { list ->
                    val favoriteIds = buildSet {
                        for (dto in list) if (repository.isFavorite(dto.id)) add(dto.id)
                    }
                    val items = list.map { dto ->
                        CourseItem(dto, dto.hasLike || dto.id in favoriteIds)
                    }
                    _state.value = _state.value.copy(
                        courses = sortByPublishDate(items, desc = true),
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

    private fun sortByPublishDate(items: List<CourseItem>, desc: Boolean): List<CourseItem> {
        return if (desc) items.sortedByDescending { it.dto.publishDate }
        else items.sortedBy { it.dto.publishDate }
    }

    fun toggleFavorite(item: CourseItem) {
        viewModelScope.launch {
            val dto = item.dto
            if (item.isFavorite) {
                repository.removeFromFavorites(dto.id)
            } else {
                repository.addToFavorites(dto)
            }
            _state.value = _state.value.copy(
                courses = _state.value.courses.map {
                    if (it.dto.id == dto.id) it.copy(isFavorite = !it.isFavorite)
                    else it
                }
            )
        }
    }
}
