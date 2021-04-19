package com.headmostlab.findmovie.data.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import com.headmostlab.findmovie.data.datasource.local.entities.Movie
import com.headmostlab.findmovie.data.datasource.local.entities.RemoteKey
import com.headmostlab.findmovie.data.datasource.local.RoomDb
import com.headmostlab.findmovie.data.datasource.local.entities.CollectionMovieCrossRef
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApiKeyProvider
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApiService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import com.headmostlab.findmovie.data.datasource.local.DataConverter as LocalDataConverter
import com.headmostlab.findmovie.data.datasource.network.DataConverter as NetworkDataConverter

@ExperimentalPagingApi
class MovieRemoteMediator(
    private val query: String,
    private val service: TMDbApiService,
    private val db: RoomDb,
    private val keyProvider: TMDbApiKeyProvider
) : RxRemoteMediator<Int, Movie>() {

    private val movieDao = db.movieDao()
    private val remoteKeyDao = db.remoteKeyDao()
    private val collectionDao = db.collectionDao()
    private val collectionMovieCrossRefDao = db.collectionMovieCrossRefDao()
    private val remoteKeyLabel = query

    override fun initializeSingle(): Single<InitializeAction> =
        Single.just(InitializeAction.SKIP_INITIAL_REFRESH)

    override fun loadSingle(
        loadType: LoadType, state: PagingState<Int, Movie>
    ): Single<MediatorResult> {
        val remoteKeySingle: Single<RemoteKey> = when (loadType) {
            REFRESH -> Single.just(RemoteKey(remoteKeyLabel, null))
            PREPEND -> return Single.just(MediatorResult.Success(true))
            APPEND -> db.remoteKeyDao().get(remoteKeyLabel)
        }
        return remoteKeySingle.subscribeOn(Schedulers.io())
            .onErrorResumeNext() {
                val remoteKey = RemoteKey(query, 1)
                remoteKeyDao.insertOrReplace(remoteKey)
                Single.just(remoteKey)
            }
            .flatMap { remoteKey ->
                val curKey = remoteKey.nextKey ?: 1

                service.getMovies(query, keyProvider.getApiKey(), curKey)
                    .map { NetworkDataConverter.map(it) }
                    .map { movies ->

                        val collection = collectionDao.get(query)

                        val collectionMovieCrossRefs =
                            movies.map { CollectionMovieCrossRef(collection.id, it.id) }

                        db.runInTransaction {
                            if (loadType == REFRESH) {
                                collectionMovieCrossRefDao.delete(collection.id)
                                remoteKeyDao.delete(remoteKeyLabel)
                            }
                            val nextKey = curKey + if (movies.isEmpty()) {
                                0
                            } else {
                                1
                            }
                            remoteKeyDao.insertOrReplace(RemoteKey(remoteKeyLabel, nextKey))
                            movieDao.insertAll(LocalDataConverter.map(movies))
                            collectionMovieCrossRefDao.insertAll(collectionMovieCrossRefs)
                        }

                        val result: MediatorResult = MediatorResult.Success(movies.isEmpty())

                        return@map result
                    }.onErrorResumeNext { e ->
                        if (e is Exception || e is HttpException) {
                            return@onErrorResumeNext Single.just(MediatorResult.Error(e))
                        }
                        return@onErrorResumeNext Single.error(e)
                    }
            }
    }
}