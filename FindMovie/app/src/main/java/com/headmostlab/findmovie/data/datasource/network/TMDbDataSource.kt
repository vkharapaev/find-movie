package com.headmostlab.findmovie.data.datasource.network

import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.domain.entity.ShortMovie
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApiService
import io.reactivex.Single

class TMDbDataSource(private val service: TMDbApiService, private val apiKeyProvider: ApiKeyProvider) {
    fun getMovies(): Single<List<ShortMovie>> =
            service.getMovies(apiKeyProvider.getApiKey()).map { DataConverter.map(it) }

    fun getMovie(id: Int): Single<FullMovie> =
            service.getMovie(id, apiKeyProvider.getApiKey()).map { DataConverter.map(it) }
}