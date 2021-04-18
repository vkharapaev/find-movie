package com.headmostlab.findmovie.ui.viewmodel.main

import androidx.lifecycle.*
import androidx.paging.rxjava2.cachedIn
import com.headmostlab.findmovie.Event
import com.headmostlab.findmovie.data.repository.PagingRepository
import com.headmostlab.findmovie.data.repository.Repository
import com.headmostlab.findmovie.domain.entity.MovieCategory
import com.headmostlab.findmovie.domain.entity.MovieWithCategory
import com.headmostlab.findmovie.domain.entity.ShortMovie
import com.headmostlab.findmovie.ui.viewmodel.main.model.UiMovieCollection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val repository: Repository,
    private val pagingRepository: PagingRepository,
    private val appStateLiveData: MutableLiveData<MainAppState> = MutableLiveData(),
    private val disposables: CompositeDisposable = CompositeDisposable(),
    private var uiMovieCollections: List<UiMovieCollection> = emptyList()
) :
    ViewModel() {

    private val _openMovieEvent: MutableLiveData<Event<Int>> = MutableLiveData()
    val openMovieEvent: LiveData<Event<Int>>
        get() = _openMovieEvent

    fun getAppStateLiveData(): LiveData<MainAppState> = appStateLiveData.also { loadMovies() }

    private fun loadMovies(reload: Boolean = false) {
        if (reload || disposables.size() == 0) {
            appStateLiveData.value = MainAppState.Loading

            repository.getCollections().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { collections ->
                    val uiMovieCollections = collections.map { collection ->
                        UiMovieCollection(
                            collection.titleResId,
                            pagingRepository.getMovies(collection.id, collection.request)
                                .cachedIn(viewModelScope)
                                .toLiveData(),
                            collection.request == MovieCategory.UPCOMING.request
                        )
                    }
                    this.uiMovieCollections = uiMovieCollections
                    appStateLiveData.value = MainAppState.Loaded(uiMovieCollections)
                }.also { disposables.add(it) }
        }
    }

    private fun getMovies(category: MovieCategory) = when (category) {
        MovieCategory.POPULAR -> repository.getPopularMovies()
        MovieCategory.NOW_PLAYING -> repository.getNowPlayingMovies()
        MovieCategory.UPCOMING -> repository.getUpcomingMovies()
    }.map { movies -> MovieWithCategory(category, movies) }

    fun clickMovieItem(movie: ShortMovie) {
        _openMovieEvent.value = Event(movie.id)
    }

    override fun onCleared() {
        disposables.clear()
    }
}