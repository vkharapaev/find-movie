package com.headmostlab.findmovie.viewmodel.main

import com.headmostlab.findmovie.Event
import com.headmostlab.findmovie.model.Movie

sealed class AppState {
    data class Success(val movies: List<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class OnMovieItemClicked(val movieId: Event<Int>) : AppState()
    object Loading : AppState()
}
