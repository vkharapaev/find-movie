package com.headmostlab.findmovie.data.repository

import com.headmostlab.findmovie.data.datasource.network.MockDataSource
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.domain.entity.ShortMovie
import io.reactivex.Single

class MockRepository(private val mockDataSource: MockDataSource = MockDataSource()) : Repository {
    override fun getMovies(): Single<List<ShortMovie>> = mockDataSource.getMovies()
    override fun getMovie(movieId: Int): Single<FullMovie> = mockDataSource.getMovie(movieId)
}