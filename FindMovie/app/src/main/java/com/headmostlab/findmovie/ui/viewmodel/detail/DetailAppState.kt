package com.headmostlab.findmovie.viewmodel.detail

import com.headmostlab.findmovie.model.FullMovie

sealed class DetailAppState {
    data class MovieLoaded(val movie: FullMovie) : DetailAppState()
    data class LoadingError(val error: Throwable) : DetailAppState()
    object Loading : DetailAppState()
}
