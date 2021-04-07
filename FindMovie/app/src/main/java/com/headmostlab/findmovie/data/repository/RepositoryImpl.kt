package com.headmostlab.findmovie.data.repository

import com.headmostlab.findmovie.data.datasource.network.TMDbDataSource
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.domain.entity.MovieCategory
import com.headmostlab.findmovie.domain.entity.ShortMovie
import io.reactivex.Single

class RepositoryImpl(private val dataSource: TMDbDataSource) : Repository {
    override fun getNowPlayingMovies(): Single<List<ShortMovie>> = dataSource.getNowPlayingMovies()
    override fun getUpcomingMovies(): Single<List<ShortMovie>> = dataSource.getUpcomingMovies()
    override fun getPopularMovies(): Single<List<ShortMovie>> = dataSource.getPopularMovies()
    override fun getMovie(movieId: Int): Single<FullMovie> = dataSource.getMovie(movieId)
    override fun getMovieCategories(): List<MovieCategory> = TODO("Not yet implemented")
}