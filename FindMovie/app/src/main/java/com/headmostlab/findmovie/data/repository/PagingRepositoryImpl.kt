package com.headmostlab.findmovie.data.repository

import androidx.paging.*
import androidx.paging.rxjava2.flowable
import com.headmostlab.findmovie.data.datasource.MovieRemoteMediator
import com.headmostlab.findmovie.data.datasource.local.DataConverter
import com.headmostlab.findmovie.data.datasource.local.RoomDb
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApiKeyProvider
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApiService
import com.headmostlab.findmovie.domain.entity.ShortMovie
import io.reactivex.Flowable
import kotlin.math.max

class PagingRepositoryImpl(
    private val service: TMDbApiService,
    private val db: RoomDb
) : PagingRepository {

    companion object {
        private const val PAGE_SIZE = 20
    }

    @ExperimentalPagingApi
    override fun getMovies(
        collectionId: Int,
        count: Int
    ): Flowable<PagingData<ShortMovie>> {
        val pageCount = max(count / PAGE_SIZE, 1)
        val mediator =
            MovieRemoteMediator(collectionId, service, db, TMDbApiKeyProvider(), pageCount)
        val pager = Pager(PagingConfig(PAGE_SIZE), remoteMediator = mediator) {
            db.movieDao().pagingSource(collectionId, count)
        }
        return pager.flowable.map { pagingData -> pagingData.map { DataConverter.map(it) } }
    }
}
