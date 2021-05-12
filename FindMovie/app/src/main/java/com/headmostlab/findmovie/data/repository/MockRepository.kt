package com.headmostlab.findmovie.data.repository

import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbMockDataSource
import com.headmostlab.findmovie.domain.entity.Person
import com.headmostlab.findmovie.domain.entity.Collection
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.domain.entity.ShortMovie
import io.reactivex.Single

class MockRepository(private val mockDataSource: TMDbMockDataSource = TMDbMockDataSource()) : Repository {
    override fun getNowPlayingMovies(): Single<List<ShortMovie>> = mockDataSource.getNowPlayingMovies()
    override fun getUpcomingMovies(): Single<List<ShortMovie>> = mockDataSource.getUpcomingMovies()
    override fun getPopularMovies(): Single<List<ShortMovie>> = mockDataSource.getPopularMovies()
    override fun getMovie(movieId: Int): Single<FullMovie> = mockDataSource.getMovie(movieId)
    override fun getCollections(): Single<List<Collection>> = TODO("Not yet implemented")
    override fun getCollection(id: Int): Single<Collection> = TODO("Not yet implemented")
    override fun getVideos(movieId: Int): Single<List<String>> = TODO("Not yet implemented")
    override fun getPeople(movieId: Int): Single<List<Person>> = TODO("Not yet implemented")
}