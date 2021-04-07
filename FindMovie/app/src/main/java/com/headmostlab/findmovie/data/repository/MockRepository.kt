package com.headmostlab.findmovie.data.repository

import com.headmostlab.findmovie.data.datasource.network.TMDbMockDataSource
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.domain.entity.MovieCategory
import com.headmostlab.findmovie.domain.entity.ShortMovie
import io.reactivex.Single

class MockRepository(private val mockDataSource: TMDbMockDataSource = TMDbMockDataSource()) : Repository {
    override fun getMovies(): Single<List<ShortMovie>> = mockDataSource.getMovies()
    override fun getMovie(movieId: Int): Single<FullMovie> = mockDataSource.getMovie(movieId)
    override fun getMovieCategories(): List<MovieCategory> = mockDataSource.getCategories()
}