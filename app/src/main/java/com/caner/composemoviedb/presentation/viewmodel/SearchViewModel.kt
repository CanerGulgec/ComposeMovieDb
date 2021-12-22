package com.caner.composemoviedb.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caner.composemoviedb.data.viewstate.Resource
import com.caner.composemoviedb.domain.usecase.SearchMovieUseCase
import com.caner.composemoviedb.view.search.state.SearchViewModelState
import com.caner.composemoviedb.view.search.state.TextEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchMovieUseCase
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val viewModelState = MutableStateFlow(SearchViewModelState())

    // UI state exposed to the UI
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        initSearch()
    }

    fun onEvent(event: TextEvent) {
        when (event) {
            is TextEvent.OnFocusChange -> {
                viewModelState.update { it.copy(isHintVisible = event.isHintVisible) }
            }

            is TextEvent.OnValueChange -> {
                viewModelState.update { it.copy(title = event.text) }
                searchQuery.value = event.text
            }
        }
    }

    private fun initSearch() {
        viewModelScope.launch {
            searchQuery.debounce(400)
                .filter { query ->
                    return@filter query.length > 2
                }
                .flatMapLatest { title ->
                    searchUseCase.execute(title)
                }.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            viewModelState.update {
                                it.copy(
                                    movies = resource.data.movies, isLoading = false
                                )
                            }
                        }
                        is Resource.Loading -> {
                            viewModelState.update {
                                it.copy(isLoading = true)
                            }
                        }
                        is Resource.Empty -> {
                            viewModelState.update {
                                it.copy(movies = emptyList())
                            }
                        }
                        else -> {
                            // Handle error state
                        }
                    }
                }
        }
    }
}