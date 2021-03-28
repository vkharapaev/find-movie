package com.headmostlab.findmovie.viewmodel.main

import com.headmostlab.findmovie.Event
import com.headmostlab.findmovie.model.ShortMovie

sealed class MainAppState {
    data class MoviesLoaded(val movies: List<ShortMovie>) : MainAppState()
    data class LoadingError(val error: Throwable) : MainAppState()
    data class OnMovieItemClicked(val movieId: Event<Int>) : MainAppState()
    object Loading : MainAppState()
}
