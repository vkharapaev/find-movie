package com.headmostlab.findmovie.model

import com.headmostlab.findmovie.model.apimodel.MovieDataSource
import io.reactivex.Single

class RepositoryImpl(private val dataSource: MovieDataSource) : Repository {
    override fun getMovies(): Single<List<ShortMovie>> {
        return dataSource.getMovies()
    }

    override fun getMovie(id: Int) : Single<FullMovie> {
        return dataSource.getMovie(id)
    }
}