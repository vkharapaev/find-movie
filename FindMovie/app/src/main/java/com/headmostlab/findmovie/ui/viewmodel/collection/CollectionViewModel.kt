package com.headmostlab.findmovie.ui.viewmodel.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.headmostlab.findmovie.Event
import com.headmostlab.findmovie.data.repository.PagingRepository
import com.headmostlab.findmovie.data.repository.Repository
import com.headmostlab.findmovie.domain.entity.ShortMovie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CollectionViewModel(
    private val collectionId: Int,
    private val repository: Repository,
    private val pagingRepository: PagingRepository,
) : ViewModel() {

    val movies get() = pagingRepository.getMovies(collectionId).toLiveData()

    val collection
        get() = repository.getCollection(collectionId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toFlowable()
            .toLiveData()

    private val _openMovieEvent: MutableLiveData<Event<Int>> = MutableLiveData()
    val openMovieEvent: LiveData<Event<Int>>
        get() = _openMovieEvent

    fun clickMovieItem(movie: ShortMovie) {
        _openMovieEvent.value = Event(movie.id)
    }
}