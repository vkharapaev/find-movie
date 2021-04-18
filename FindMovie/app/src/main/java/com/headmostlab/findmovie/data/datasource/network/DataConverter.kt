package com.headmostlab.findmovie.data.datasource.network

import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.movie.ApiFullMovie
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.movie.ApiGenre
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.popular.ApiMovies
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.popular.ApiShortMovie
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.domain.entity.ShortMovie

object DataConverter {

    private val dummyShortMovie = ShortMovie(0, "", "", 0.0, 0.0, "")

    fun map(movies: ApiMovies): List<ShortMovie> =
        movies.results.map { map(it) }.filter { it != dummyShortMovie }

    private fun map(movie: ApiShortMovie): ShortMovie {
        return try {
            ShortMovie(
                movie.id,
                movie.title,
                movie.releaseDate,
                movie.voteAverage,
                movie.popularity,
                movie.posterPath
            )
        } catch (e: Throwable) {
            return dummyShortMovie
        }
    }

    fun map(movie: ApiFullMovie): FullMovie {
        return FullMovie(
            movie.id,
            movie.title,
            movie.originalTitle,
            map(movie.genres),
            movie.runtime ?: 0,
            movie.popularity,
            movie.voteAverage,
            movie.voteCount,
            movie.budget,
            movie.revenue,
            movie.releaseDate,
            movie.overview ?: "",
            movie.posterPath ?: ""
        )
    }

    fun map(genres: List<ApiGenre>): List<String> = genres.map { it.name }
}