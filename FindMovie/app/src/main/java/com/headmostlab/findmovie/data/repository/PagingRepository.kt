package com.headmostlab.findmovie.data.repository

import androidx.paging.PagingData
import com.headmostlab.findmovie.domain.entity.ShortMovie
import io.reactivex.Flowable

interface PagingRepository {
    fun getMovies(
        collectionId: Int,
        count: Int = 500
    ): Flowable<PagingData<ShortMovie>>
}
