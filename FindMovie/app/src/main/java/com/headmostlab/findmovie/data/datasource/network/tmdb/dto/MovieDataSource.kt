package com.headmostlab.findmovie.model.apimodel

import com.headmostlab.findmovie.model.FullMovie
import com.headmostlab.findmovie.model.ShortMovie
import com.headmostlab.findmovie.model.apimodel.movie.ApiFullMovie
import com.headmostlab.findmovie.model.apimodel.movie.ApiGenre
import com.headmostlab.findmovie.model.apimodel.popular.ApiMovies
import com.headmostlab.findmovie.model.apimodel.popular.ApiShortMovie
import com.headmostlab.findmovie.network.ApiKeyProvider
import com.headmostlab.findmovie.network.tmdb.TMDbApiService
import io.reactivex.Single

class MovieDataSource(
    private val service: TMDbApiService,
    private val apiKeyProvider: ApiKeyProvider
) {
    fun getMovies(): Single<List<ShortMovie>> =
        service.getMovies(apiKeyProvider.getApiKey()).map { map(it) }

    fun getMovie(id: Int): Single<FullMovie> {
        return service.getMovie(id, apiKeyProvider.getApiKey()).map { map(it) }
    }

    private fun map(movies: ApiMovies): List<ShortMovie> =
        movies.results.map { map(it) }

    private fun map(movie: ApiShortMovie): ShortMovie {
        return ShortMovie(
            movie.id,
            movie.title,
            movie.releaseDate.substring(0..3).toInt(),
            movie.popularity,
            movie.posterPath
        )
    }

    private fun map(movie: ApiFullMovie): FullMovie {
        return FullMovie(
            movie.id,
            movie.title,
            movie.originalTitle,
            map(movie.genres),
            movie.runtime ?: 0,
            movie.popularity,
            movie.budget,
            movie.revenue,
            movie.releaseDate.substring(0..3).toInt(),
            movie.overview ?: "",
            movie.posterPath ?: ""
        )
    }

    private fun map(genres: List<ApiGenre>): List<String> = genres.map { it.name }

}