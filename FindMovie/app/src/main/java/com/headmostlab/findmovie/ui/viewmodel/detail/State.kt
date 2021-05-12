package com.headmostlab.findmovie.ui.viewmodel.detail

import com.headmostlab.findmovie.domain.entity.FullMovie

sealed class State {
    data class Success(val movie: FullMovie) : State()
    data class Error(val error: Throwable) : State()
    object Loading : State()
}
