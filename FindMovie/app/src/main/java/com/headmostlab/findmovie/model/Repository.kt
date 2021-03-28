package com.headmostlab.findmovie.model

interface Repository {
    fun getMovies(): List<ShortMovie>
    fun getMovie(movieId: Int): FullMovie
}