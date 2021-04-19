package com.headmostlab.findmovie.data.datasource.local.dao

import androidx.room.*
import com.headmostlab.findmovie.data.datasource.local.entities.CollectionMovieCrossRef

@Dao
interface CollectionMovieCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(refs: List<CollectionMovieCrossRef>)

    @Query("DELETE FROM CollectionMovieCrossRef WHERE collectionId = :collectionId")
    fun delete(collectionId: Int)
}