package com.headmostlab.findmovie.di

import android.content.Context
import androidx.room.Room
import com.headmostlab.findmovie.data.datasource.local.RoomDb
import com.headmostlab.findmovie.data.datasource.network.tmdb.*
import com.headmostlab.findmovie.data.repository.PagingRepository
import com.headmostlab.findmovie.data.repository.PagingRepositoryImpl
import com.headmostlab.findmovie.data.repository.Repository
import com.headmostlab.findmovie.data.repository.RepositoryImpl
import dagger.*
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun repository(): Repository

    fun pagingRepository(): PagingRepository

    fun db(): RoomDb

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appContext(context: Context): Builder
        fun build(): AppComponent
    }
}

@Module
abstract class AppModule {

    @Binds
    abstract fun provideApiKeyProvider(apiKeyProvider: TMDbApiKeyProvider): ApiKeyProvider

    @Binds
    abstract fun provideHostProvider(hostProvider: TMDbHostProvider): HostProvider

    @Binds
    abstract fun provideRepository(repository: RepositoryImpl): Repository

    @Binds
    abstract fun providePagingRepository(pagingRepository: PagingRepositoryImpl): PagingRepository

    companion object {
        private const val DB_NAME = "moviedb"

        @Singleton
        @Provides
        fun provideApi(hostProvider: HostProvider): TMDbApiService =
            TMDbApi(hostProvider).getService()

        @Singleton
        @Provides
        fun provideDb(context: Context): RoomDb =
            Room.databaseBuilder(context, RoomDb::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}
