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
        movieQuery: String,
        count: Int
    ): Flowable<PagingData<ShortMovie>> {
        val mediator =
            MovieRemoteMediator(movieQuery, service, db, TMDbApiKeyProvider(), count / PAGE_SIZE)
        val pager = Pager(PagingConfig(PAGE_SIZE), remoteMediator = mediator) {
            db.movieDao().pagingSource(collectionId, count)
        }
        return pager.flowable.map { pagingData -> pagingData.map { DataConverter.map(it) } }
    }
}
