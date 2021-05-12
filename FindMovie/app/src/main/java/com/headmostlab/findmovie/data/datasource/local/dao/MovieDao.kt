package com.headmostlab.findmovie.data.datasource.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.headmostlab.findmovie.data.datasource.local.entities.Movie
import io.reactivex.Single

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(movies: List<Movie>)

    @Query("DELETE FROM Movie")
    fun deleteAll()

    @Query("SELECT * FROM Movie")
    fun getAllMovies(): Single<List<Movie>>

    @Query("SELECT m.* from CollectionMovieCrossRef ref INNER JOIN Movie m on ref.movieId = m.id WHERE ref.collectionId = :collectionId ORDER BY m.popularity DESC limit :maxCount")
    fun pagingSource(collectionId: Int, maxCount: Int = 1_000_000): PagingSource<Int, Movie>
}
