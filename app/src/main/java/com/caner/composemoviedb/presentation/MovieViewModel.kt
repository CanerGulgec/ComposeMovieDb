package com.caner.composemoviedb.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.caner.composemoviedb.domain.paging.MoviesPagingSource
import com.caner.composemoviedb.common.Constants
import com.caner.composemoviedb.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        val movieType = savedStateHandle.get<Int>(Constants.MOVIE_TYPE) ?: 1
        MoviesPagingSource.movieType = movieType
    }

    val moviePagingFlow = movieRepository.getMovies()
        .cachedIn(viewModelScope)
}