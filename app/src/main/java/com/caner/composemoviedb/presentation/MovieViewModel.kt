package com.caner.composemoviedb.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.caner.composemoviedb.domain.paging.MoviesPagingSource
import com.caner.composemoviedb.common.Constants
import com.caner.composemoviedb.common.Resource
import com.caner.composemoviedb.data.MovieDetailModel
import com.caner.composemoviedb.data.MovieModel
import com.caner.composemoviedb.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _popularMovieState = MutableStateFlow<Resource<MovieModel>>(Resource.Initial)
    val popularMovieState: StateFlow<Resource<MovieModel>> get() = _popularMovieState

    val moviePagingFlow =
        movieRepository.getMovies(Constants.NOW_PLAYING_MOVIES).cachedIn(viewModelScope)

    fun getPopularMovies() {
        viewModelScope.launch {
            movieRepository.getPopularMovies()
                .collect {
                    _popularMovieState.value = it
                }
        }
    }
}