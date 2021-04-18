package com.headmostlab.findmovie.ui.viewmodel.main.model

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.headmostlab.findmovie.domain.entity.ShortMovie

data class UiMovieCollection(
    val title: Int,
    val movies: LiveData<PagingData<ShortMovie>>,
    val showSecondLayout: Boolean = false
)
