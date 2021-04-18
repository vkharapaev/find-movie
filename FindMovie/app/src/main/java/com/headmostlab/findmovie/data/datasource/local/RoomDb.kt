package com.headmostlab.findmovie.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.headmostlab.findmovie.data.datasource.local.dao.CollectionDao
import com.headmostlab.findmovie.data.datasource.local.dao.CollectionMovieCrossRefDao
import com.headmostlab.findmovie.data.datasource.local.dao.MovieDao
import com.headmostlab.findmovie.data.datasource.local.dao.RemoteKeyDao
import com.headmostlab.findmovie.data.datasource.local.entities.Collection
import com.headmostlab.findmovie.data.datasource.local.entities.CollectionMovieCrossRef
import com.headmostlab.findmovie.data.datasource.local.entities.Movie
import com.headmostlab.findmovie.data.datasource.local.entities.RemoteKey

@Database(
    entities = [Collection::class, Movie::class, CollectionMovieCrossRef::class, RemoteKey::class],
    version = 1,
    exportSchema = true
)
abstract class RoomDb : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeyDao(): RemoteKeyDao
    abstract fun collectionDao(): CollectionDao
    abstract fun collectionMovieCrossRefDao(): CollectionMovieCrossRefDao
}
