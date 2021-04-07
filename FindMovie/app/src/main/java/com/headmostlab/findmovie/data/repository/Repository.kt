package com.headmostlab.findmovie.model

import io.reactivex.Single

interface Repository {
    fun getMovies(): Single<List<ShortMovie>>
    fun getMovie(movieId: Int): Single<FullMovie>
}