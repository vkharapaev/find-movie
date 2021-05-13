package com.headmostlab.findmovie.ui.viewmodel.main

import androidx.lifecycle.*
import androidx.paging.insertFooterItem
import androidx.paging.map
import androidx.paging.rxjava2.cachedIn
import com.headmostlab.findmovie.Event
import com.headmostlab.findmovie.data.repository.PagingRepository
import com.headmostlab.findmovie.data.repository.Repository
import com.headmostlab.findmovie.domain.entity.Collection
import com.headmostlab.findmovie.domain.entity.ECollection
import com.headmostlab.findmovie.domain.entity.ShortMovie
import com.headmostlab.findmovie.ui.viewmodel.main.model.UiItem
import com.headmostlab.findmovie.ui.viewmodel.main.model.UiMovieCollection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val pagingRepository: PagingRepository
) : ViewModel() {

    private companion object {
        const val MAX_MOVIE_COUNT_IN_ROW = 15
    }

    private val uiCollectionsLiveData: MutableLiveData<List<UiMovieCollection>> = MutableLiveData()
    private val disposables: CompositeDisposable = CompositeDisposable()

    private val _openMovieEvent: MutableLiveData<Event<Int>> = MutableLiveData()
    val openMovieEvent: LiveData<Event<Int>>
        get() = _openMovieEvent

    private val _openCollectionEvent: MutableLiveData<Event<Int>> = MutableLiveData()
    val openCollectionEvent: LiveData<Event<Int>>
        get() = _openCollectionEvent

    fun getCollections(): LiveData<List<UiMovieCollection>> =
        uiCollectionsLiveData.also { loadMovies() }

    fun loadMovies(reload: Boolean = false) {
        if (reload || disposables.size() == 0) {
            disposables.clear()

            repository.getCollections().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { collections ->
                    uiCollectionsLiveData.value = collections.map { collection ->
                        UiMovieCollection(
                            collection,
                            pagingRepository.getMovies(
                                collection.id,
                                MAX_MOVIE_COUNT_IN_ROW
                            )
                                .map { pagingData -> pagingData.map { UiItem.Movie(it) as UiItem } }
                                .map { it.insertFooterItem(item = UiItem.Footer(collection)) }
                                .cachedIn(viewModelScope).toLiveData(),
                            collection.request == ECollection.UPCOMING.request
                        )
                    }
                }.also { disposables.add(it) }
        }
    }

    fun clickMovieItem(movie: ShortMovie) {
        _openMovieEvent.value = Event(movie.id)
    }

    fun selectCollection(collection: Collection) {
        _openCollectionEvent.value = Event(collection.id)
    }

    override fun onCleared() {
        disposables.clear()
    }
}