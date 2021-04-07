package com.headmostlab.findmovie.data.repository

import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.domain.entity.ShortMovie
import io.reactivex.Single

interface Repository {
    fun getMovies(): Single<List<ShortMovie>>
    fun getMovie(movieId: Int): Single<FullMovie>
}