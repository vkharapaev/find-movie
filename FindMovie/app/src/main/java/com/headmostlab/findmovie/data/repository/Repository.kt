package com.headmostlab.findmovie.data.repository

import com.headmostlab.findmovie.domain.entity.Collection
import com.headmostlab.findmovie.domain.entity.ECollection
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.domain.entity.ShortMovie
import io.reactivex.Single

interface Repository {
    fun getNowPlayingMovies(): Single<List<ShortMovie>>
    fun getUpcomingMovies(): Single<List<ShortMovie>>
    fun getPopularMovies(): Single<List<ShortMovie>>
    fun getMovie(movieId: Int): Single<FullMovie>
    fun getCollections(): Single<List<Collection>>
    fun getCollection(id: Int): Single<Collection>
}