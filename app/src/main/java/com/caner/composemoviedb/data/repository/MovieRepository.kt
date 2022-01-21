package com.caner.composemoviedb.data.repository

import androidx.paging.PagingData
import com.caner.composemoviedb.utils.network.Resource
import com.caner.composemoviedb.data.model.Movie
import com.caner.composemoviedb.data.model.remote.MoviesResponse
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getNowPlayingMovies(): Flow<PagingData<Movie>>
    suspend fun getPopularMovies(): Resource<MoviesResponse>
}