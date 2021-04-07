package com.headmostlab.findmovie.data.repository

import com.headmostlab.findmovie.data.datasource.network.TMDbDataSource
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.domain.entity.MovieCategory
import com.headmostlab.findmovie.domain.entity.ShortMovie
import io.reactivex.Single

class RepositoryImpl(private val dataSource: TMDbDataSource) : Repository {
    override fun getMovies(): Single<List<ShortMovie>> {
        return dataSource.getMovies()
    }

    override fun getMovie(id: Int) : Single<FullMovie> {
        return dataSource.getMovie(id)
    }

    override fun getMovieCategories(): List<MovieCategory> {
        TODO("Not yet implemented")
    }
}