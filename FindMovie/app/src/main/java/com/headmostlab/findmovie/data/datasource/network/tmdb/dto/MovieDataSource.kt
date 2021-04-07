package com.headmostlab.findmovie.data.datasource.network.tmdb.dto

import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.domain.entity.ShortMovie
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.movie.ApiFullMovie
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.movie.ApiGenre
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.popular.ApiMovies
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.popular.ApiShortMovie
import com.headmostlab.findmovie.data.datasource.network.ApiKeyProvider
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApiService
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