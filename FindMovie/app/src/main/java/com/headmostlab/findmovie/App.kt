package com.headmostlab.findmovie

import android.app.Application
import com.headmostlab.findmovie.data.datasource.local.entities.Collection
import com.headmostlab.findmovie.di.DaggerAppComponent
import com.headmostlab.findmovie.domain.entity.ECollection
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        initDI()

        createCollectionTypes()
    }

    private fun initDI() {
        DI.appComponent = DaggerAppComponent.builder().appContext(this).build()
    }

    private fun createCollectionTypes() {
        Single.fromCallable {
            val collections = ECollection.values()
                .map { Collection(it.ordinal + 1, it.name, it.request) }

            DI.appComponent.db().collectionDao().insertAll(collections)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}