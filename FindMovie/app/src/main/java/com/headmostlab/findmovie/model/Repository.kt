package com.headmostlab.findmovie.model

interface Repository {
    fun getMovies(): List<Movie>
}