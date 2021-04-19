package com.headmostlab.findmovie.ui.viewmodel.main

import com.headmostlab.findmovie.ui.viewmodel.main.model.UiMovieCollection

sealed class MainAppState {
    data class Loaded(val movies: List<UiMovieCollection>) : MainAppState()
    data class Error(val error: Throwable) : MainAppState()
    object Loading : MainAppState()
}
