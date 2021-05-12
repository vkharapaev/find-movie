package com.headmostlab.findmovie.data.repository

import com.headmostlab.findmovie.data.datasource.local.DataConverter
import com.headmostlab.findmovie.data.datasource.local.RoomDb
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbDataSource
import com.headmostlab.findmovie.domain.entity.Person
import com.headmostlab.findmovie.domain.entity.Collection
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.domain.entity.ShortMovie
import io.reactivex.Single

class RepositoryImpl(private val dataSource: TMDbDataSource, private val db: RoomDb) : Repository {
    override fun getNowPlayingMovies(): Single<List<ShortMovie>> = dataSource.getNowPlayingMovies()
    override fun getUpcomingMovies(): Single<List<ShortMovie>> = dataSource.getUpcomingMovies()
    override fun getPopularMovies(): Single<List<ShortMovie>> = dataSource.getPopularMovies()
    override fun getMovie(movieId: Int): Single<FullMovie> = dataSource.getMovie(movieId)
    override fun getCollections(): Single<List<Collection>> =
        db.collectionDao().getAll().map { it -> it.map { DataConverter.map(it) } }
    override fun getCollection(id: Int): Single<Collection> =
        db.collectionDao().get(id).map { DataConverter.map(it) }
    override fun getVideos(movieId: Int): Single<List<String>> = dataSource.getVideos(movieId)
    override fun getPeople(movieId: Int): Single<List<Person>> = dataSource.getPeople(movieId)
}