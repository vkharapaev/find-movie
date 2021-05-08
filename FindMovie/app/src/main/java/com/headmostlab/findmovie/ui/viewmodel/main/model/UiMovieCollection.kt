package com.headmostlab.findmovie.ui.viewmodel.main.model

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.headmostlab.findmovie.domain.entity.Collection
import com.headmostlab.findmovie.domain.entity.ShortMovie

data class UiMovieCollection(
    val collection: Collection,
    val movies: LiveData<PagingData<UiItem>>,
    val showSecondLayout: Boolean = false
)
