package com.headmostlab.findmovie.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.headmostlab.findmovie.data.datasource.local.entities.RemoteKey
import io.reactivex.Single

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(remoteKey: RemoteKey)

    @Query("SELECT * FROM RemoteKey WHERE label = :label")
    fun get(label: String): Single<RemoteKey>

    @Query("DELETE FROM RemoteKey WHERE label = :label")
    fun delete(label: String)
}
