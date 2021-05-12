package com.headmostlab.findmovie.data.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import com.headmostlab.findmovie.data.datasource.local.entities.Movie
import com.headmostlab.findmovie.data.datasource.local.entities.RemoteKey
import com.headmostlab.findmovie.data.datasource.local.RoomDb
import com.headmostlab.findmovie.data.datasource.local.entities.Collection
import com.headmostlab.findmovie.data.datasource.local.entities.CollectionMovieCrossRef
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApiKeyProvider
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApiService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import com.headmostlab.findmovie.data.datasource.local.DataConverter as LocalDataConverter
import com.headmostlab.findmovie.data.datasource.network.tmdb.DataConverter as NetworkDataConverter

@ExperimentalPagingApi
class MovieRemoteMediator(
    private val collectionId: Int,
    private val service: TMDbApiService,
    private val db: RoomDb,
    private val keyProvider: TMDbApiKeyProvider,
    private val maxPageCount: Int? = null
) : RxRemoteMediator<Int, Movie>() {

    private companion object {
        private const val INITIAL_PAGE_NUMBER = 1
    }

    private val movieDao = db.movieDao()
    private val remoteKeyDao = db.remoteKeyDao()
    private val collectionDao = db.collectionDao()
    private val collectionMovieCrossRefDao = db.collectionMovieCrossRefDao()
    private lateinit var remoteKeyLabel: String
    private lateinit var query: String

    override fun initializeSingle(): Single<InitializeAction> =
        Single.just(InitializeAction.SKIP_INITIAL_REFRESH)

    override fun loadSingle(
        loadType: LoadType, state: PagingState<Int, Movie>
    ): Single<MediatorResult> {

        val collection: Single<Collection> = when (loadType) {
            PREPEND -> return Single.just(MediatorResult.Success(true))
            else -> db.collectionDao().get(collectionId)
        }

        return collection.subscribeOn(Schedulers.io())
            .flatMap { collection ->
                remoteKeyLabel = collection.request
                query = collection.request

                when (loadType) {
                    REFRESH -> Single.just(RemoteKey(collection.request, null))
                    else -> db.remoteKeyDao().get(collection.request) // APPEND
                }
            }
            .onErrorResumeNext {
                Single.just(RemoteKey(query, null))
            }.flatMap { remoteKey ->
                val curKey = remoteKey.nextKey ?: INITIAL_PAGE_NUMBER

                if (maxPageCount != null && curKey > maxPageCount) {
                    return@flatMap Single.just(MediatorResult.Success(true))
                }

                service.getMovies(query, keyProvider.getApiKey(), curKey)
                    .map { NetworkDataConverter.map(it) }
                    .map { movies ->

                        val collection = collectionDao.getByRequest(query)

                        val collectionMovieCrossRefs =
                            movies.map { CollectionMovieCrossRef(collection.id, it.id) }

                        db.runInTransaction {
                            if (loadType == REFRESH) {
                                collectionMovieCrossRefDao.delete(collection.id)
                                remoteKeyDao.delete(remoteKeyLabel)
                            }

                            val nextKey = curKey + if (movies.isEmpty()) 0 else 1

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