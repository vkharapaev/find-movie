package com.headmostlab.findmovie.viewmodel

import com.headmostlab.findmovie.model.Movie

sealed class AppState {
    data class Success(val movies: List<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading: AppState()
}
