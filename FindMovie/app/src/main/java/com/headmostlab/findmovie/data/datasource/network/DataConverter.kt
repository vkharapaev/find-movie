package com.headmostlab.findmovie.data.datasource.network

import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.movie.ApiFullMovie
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.movie.ApiGenre
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.popular.ApiMovies
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.popular.ApiShortMovie
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.domain.entity.ShortMovie

object DataConverter {
    fun map(movies: ApiMovies): List<ShortMovie> =
            movies.results.map { map(it) }

    private fun map(movie: ApiShortMovie): ShortMovie {
        return ShortMovie(
                movie.id,
                movie.title,
                movie.releaseDate,
                movie.voteAverage,
                movie.posterPath
        )
    }

    fun map(movie: ApiFullMovie): FullMovie {
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

    fun map(genres: List<ApiGenre>): List<String> = genres.map { it.name }
}