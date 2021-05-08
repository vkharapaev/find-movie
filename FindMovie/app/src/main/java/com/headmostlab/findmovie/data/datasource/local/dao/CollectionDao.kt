package com.headmostlab.findmovie.data.datasource.local.dao

import androidx.room.*
import com.headmostlab.findmovie.data.datasource.local.entities.Collection
import io.reactivex.Single

@Dao
interface CollectionDao {
    @Query("SELECT * FROM Collection")
    fun getAll(): Single<List<Collection>>

    @Query("SELECT * FROM Collection WHERE request = :rq")
    fun getByRequest(rq: String): Collection

    @Query("SELECT * FROM Collection WHERE id = :id")
    fun get(id: Int): Single<Collection>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(collections: List<Collection>)

    @Delete
    fun delete(collection: Collection)
}