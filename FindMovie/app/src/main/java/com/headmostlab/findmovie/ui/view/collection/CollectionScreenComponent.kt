package com.headmostlab.findmovie.ui.view.collection

import androidx.lifecycle.ViewModel
import com.headmostlab.findmovie.data.repository.PagingRepository
import com.headmostlab.findmovie.data.repository.Repository
import com.headmostlab.findmovie.di.ScreenScope
import com.headmostlab.findmovie.di.ViewModelFactory
import com.headmostlab.findmovie.di.ViewModelKey
import com.headmostlab.findmovie.ui.viewmodel.collection.CollectionViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap

@ScreenScope
@Component(modules = [CollectionScreenModule::class])
interface CollectionScreenComponent {
    fun viewModelFactory(): ViewModelFactory

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun collectionId(id: Int): Builder

        @BindsInstance
        fun repository(repository: Repository): Builder

        @BindsInstance
        fun pagingRepository(pagingRepository: PagingRepository): Builder

        fun build(): CollectionScreenComponent
    }
}

@Module
interface CollectionScreenModule {

    @Binds
    @IntoMap
    @ViewModelKey(CollectionViewModel::class)
    fun provideViewModel(viewModel: CollectionViewModel): ViewModel
}
