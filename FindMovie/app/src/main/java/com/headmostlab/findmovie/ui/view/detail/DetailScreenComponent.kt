package com.headmostlab.findmovie.ui.view.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import com.headmostlab.findmovie.data.datasource.network.youtube.YouTubeDataSource
import com.headmostlab.findmovie.data.datasource.network.youtube.YouTubeDataSourceImpl
import com.headmostlab.findmovie.data.repository.Repository
import com.headmostlab.findmovie.data.repository.VideoRepository
import com.headmostlab.findmovie.data.repository.VideoRepositoryImpl
import com.headmostlab.findmovie.di.ScreenScope
import com.headmostlab.findmovie.di.ViewModelFactory
import com.headmostlab.findmovie.di.ViewModelKey
import com.headmostlab.findmovie.ui.viewmodel.detail.DetailViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap

@ScreenScope
@Component(modules = [DetailScreenModule::class])
interface DetailScreenComponent {
    fun viewModelFactory(): ViewModelFactory

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appContext(context: Context): Builder

        @BindsInstance
        fun repository(repository: Repository): Builder

        fun build(): DetailScreenComponent
    }
}

@Module
interface DetailScreenModule {

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    fun provideViewModel(viewModel: DetailViewModel): ViewModel

    @Binds
    fun provideYouTubeDataSource(youTubeDataSource: YouTubeDataSourceImpl): YouTubeDataSource

    @Binds
    fun provideVideoRepository(videoRepository: VideoRepositoryImpl): VideoRepository
}
