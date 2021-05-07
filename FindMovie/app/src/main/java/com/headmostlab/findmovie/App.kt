package com.headmostlab.findmovie

import android.app.Application
import androidx.room.Room
import com.headmostlab.findmovie.data.datasource.local.RoomDb
import com.headmostlab.findmovie.data.datasource.local.entities.Collection
import com.headmostlab.findmovie.domain.entity.ECollection
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class App : Application() {

    val database: RoomDb by lazy {
        Room.databaseBuilder(this, RoomDb::class.java, DB_NAME).build()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        createCollectionTypes()
    }

    private fun createCollectionTypes() {
        Single.fromCallable {
            val collections = ECollection.values()
                .map { Collection(it.ordinal + 1, it.name, it.request) }

            database.collectionDao().insertAll(collections)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    companion object {
        private const val DB_NAME = "moviedb"
        lateinit var instance: App
            private set
    }
}