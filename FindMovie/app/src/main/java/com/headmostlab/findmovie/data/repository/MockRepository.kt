package com.headmostlab.findmovie.data.repository

import com.headmostlab.findmovie.data.datasource.network.TMDbMockDataSource
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.domain.entity.MovieCategory
import com.headmostlab.findmovie.domain.entity.ShortMovie
import io.reactivex.Single

class MockRepository(private val mockDataSource: TMDbMockDataSource = TMDbMockDataSource()) : Repository {
    override fun getNowPlayingMovies(): Single<List<ShortMovie>> = mockDataSource.getNowPlayingMovies()
    override fun getUpcomingMovies(): Single<List<ShortMovie>> = mockDataSource.getUpcomingMovies()
    override fun getPopularMovies(): Single<List<ShortMovie>> = mockDataSource.getPopularMovies()
    override fun getMovie(movieId: Int): Single<FullMovie> = mockDataSource.getMovie(movieId)
    override fun getMovieCategories(): List<MovieCategory> = mockDataSource.getCategories()
}