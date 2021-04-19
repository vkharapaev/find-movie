package com.headmostlab.findmovie.ui.viewmodel.detail

import com.headmostlab.findmovie.domain.entity.FullMovie

sealed class DetailAppState {
    data class MovieLoaded(val movie: FullMovie) : DetailAppState()
    data class LoadingError(val error: Throwable) : DetailAppState()
    object Loading : DetailAppState()
}
