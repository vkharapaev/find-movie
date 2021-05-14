package com.headmostlab.findmovie.data.datasource.network.tmdb

import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.credit.ApiCredits
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.movie.ApiFullMovie
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.movie.ApiGenre
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.movie.ApiMovies
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.movie.ApiShortMovie
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.video.ApiVideos
import com.headmostlab.findmovie.domain.entity.Person
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.domain.entity.ShortMovie
import java.util.*

object DataConverter {

    private val dummyShortMovie = ShortMovie(0, "", "", 0.0, 0.0, "", "")

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
                movie.posterPath,
                movie.backdropPath
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

    fun map(apiVideos: ApiVideos): List<String> =
        apiVideos.videos.filter { it.site.toUpperCase(Locale.getDefault()) == "YOUTUBE" }
            .map { it.key }

    fun map(apiCredits: ApiCredits): List<Person> {
        val people = mutableListOf<Person>()
        people.addAll(apiCredits.cast.filter { !it.profilePath.isNullOrBlank() }
            .map { Person(it.id, it.name, "Actor", it.profilePath) })
        people.addAll(apiCredits.crew.filter { !it.profilePath.isNullOrBlank() }
            .map { Person(it.id, it.name, it.job, it.profilePath) })
        return people
    }
}