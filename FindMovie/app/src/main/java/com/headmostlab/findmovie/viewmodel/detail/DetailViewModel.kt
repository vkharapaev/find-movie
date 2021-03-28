package com.headmostlab.findmovie.viewmodel.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.headmostlab.findmovie.model.FullMovie
import com.headmostlab.findmovie.model.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailViewModel(
    private val repository: Repository,
    private val appState: MutableLiveData<DetailAppState> = MutableLiveData(),
    private val disposables: CompositeDisposable = CompositeDisposable(),
    private var movie: FullMovie? = null
) : ViewModel() {

    fun getAppState(movieId: Int): LiveData<DetailAppState> {
        loadMovie(movieId)
        return appState
    }

    private fun loadMovie(movieId: Int) {
        val movie = this.movie
        movie?.let {
            appState.value = DetailAppState.MovieLoaded(movie)
            return
        }
        appState.value = DetailAppState.Loading
        disposables.add(
            repository.getMovie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    this.movie = it
                    appState.value = DetailAppState.MovieLoaded(it)
                }, { appState.value = DetailAppState.LoadingError(it) })
        )
    }

    override fun onCleared() {
        disposables.clear()
    }
}