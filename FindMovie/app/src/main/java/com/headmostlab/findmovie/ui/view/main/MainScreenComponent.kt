package com.headmostlab.findmovie.ui.view.main

import androidx.lifecycle.ViewModel
import com.headmostlab.findmovie.data.repository.PagingRepository
import com.headmostlab.findmovie.data.repository.Repository
import com.headmostlab.findmovie.di.ScreenScope
import com.headmostlab.findmovie.di.ViewModelFactory
import com.headmostlab.findmovie.di.ViewModelKey
import com.headmostlab.findmovie.ui.viewmodel.main.MainViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap

@ScreenScope
@Component(modules = [MainScreenModule::class])
interface MainScreenComponent {
    fun viewModelFactory(): ViewModelFactory

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun repository(repository: Repository): Builder

        @BindsInstance
        fun pagingRepository(pagingRepository: PagingRepository): Builder

        fun build(): MainScreenComponent
    }
}

@Module
interface MainScreenModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun provideViewModel(viewModel: MainViewModel): ViewModel
}
