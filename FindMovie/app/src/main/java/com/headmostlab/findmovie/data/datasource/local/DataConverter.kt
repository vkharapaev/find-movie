package com.headmostlab.findmovie.data.datasource.local

import com.headmostlab.findmovie.data.datasource.local.entities.Movie
import com.headmostlab.findmovie.domain.entity.ShortMovie

object DataConverter {

    fun map(movie: ShortMovie) =
        Movie(movie.id, movie.title, movie.date, movie.rating, movie.popularity, movie.poster, movie.backdrop)

    fun map(movies: List<ShortMovie>): List<Movie> = movies.map { map(it) }

    fun map(movie: Movie) =
        ShortMovie(
            movie.id,
            movie.title,
            movie.date,
            movie.rating,
            movie.popularity,
            movie.poster,
            movie.backdrop
        )
}
