package com.headmostlab.findmovie.ui.viewmodel.main

import androidx.lifecycle.*
import androidx.paging.rxjava2.cachedIn
import com.headmostlab.findmovie.Event
import com.headmostlab.findmovie.data.repository.PagingRepository
import com.headmostlab.findmovie.data.repository.Repository
import com.headmostlab.findmovie.domain.entity.ECollection
import com.headmostlab.findmovie.domain.entity.ShortMovie
import com.headmostlab.findmovie.ui.viewmodel.main.model.UiMovieCollection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val repository: Repository,
    private val pagingRepository: PagingRepository,
    private val appStateLiveData: MutableLiveData<List<UiMovieCollection>> = MutableLiveData(),
    private val disposables: CompositeDisposable = CompositeDisposable()
) : ViewModel() {

    private companion object {
        const val MAX_MOVIE_COUNT_IN_ROW = 20
    }

    private val _openMovieEvent: MutableLiveData<Event<Int>> = MutableLiveData()
    val openMovieEvent: LiveData<Event<Int>>
        get() = _openMovieEvent

    fun getAppStateLiveData(): LiveData<List<UiMovieCollection>> =
        appStateLiveData.also { loadMovies() }

    fun loadMovies(reload: Boolean = false) {
        if (reload || disposables.size() == 0) {
            disposables.clear()

            repository.getCollections().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { collections ->
                    appStateLiveData.value = collections.map { collection ->
                        UiMovieCollection(
                            collection.id,
                            ECollection.valueOf(collection.collectionRid).title,
                            pagingRepository.getMovies(
                                collection.id,
                                collection.request,
                                MAX_MOVIE_COUNT_IN_ROW
                            ).cachedIn(viewModelScope).toLiveData(),
                            collection.request == ECollection.UPCOMING.request
                        )
                    }
                }.also { disposables.add(it) }
        }
    }

    fun clickMovieItem(movie: ShortMovie) {
        _openMovieEvent.value = Event(movie.id)
    }

    override fun onCleared() {
        disposables.clear()
    }
}