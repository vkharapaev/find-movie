package com.headmostlab.findmovie.data.datasource.network.tmdb

import com.headmostlab.findmovie.domain.entity.Person
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.domain.entity.ShortMovie
import io.reactivex.Single

class TMDbDataSource(
    private val service: TMDbApiService,
    private val apiKeyProvider: ApiKeyProvider
) {
    fun getNowPlayingMovies(): Single<List<ShortMovie>> =
        service.getNowPlayingMovies(apiKeyProvider.getApiKey()).map { DataConverter.map(it) }

    fun getUpcomingMovies(): Single<List<ShortMovie>> =
        service.getUpcomingMovies(apiKeyProvider.getApiKey()).map { DataConverter.map(it) }

    fun getPopularMovies(): Single<List<ShortMovie>> =
        service.getPopularMovies(apiKeyProvider.getApiKey()).map { DataConverter.map(it) }

    fun getMovie(id: Int): Single<FullMovie> =
        service.getMovie(id, apiKeyProvider.getApiKey()).map { DataConverter.map(it) }

    fun getVideos(movieId: Int): Single<List<String>> =
        service.getVideos(movieId, apiKeyProvider.getApiKey()).map { DataConverter.map(it) }

    fun getPeople(movieId: Int): Single<List<Person>> =
        service.getCredits(movieId, apiKeyProvider.getApiKey()).map { DataConverter.map(it) }
}