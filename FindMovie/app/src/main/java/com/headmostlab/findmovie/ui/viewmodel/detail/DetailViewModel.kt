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

    fun getAppState(movieId: Int): LiveData<DetailAppState> = appState.also { loadMovie(movieId) }

    private fun loadMovie(movieId: Int) {
        movie?.let {
            appState.value = DetailAppState.MovieLoaded(it)
            return
        }
        appState.value = DetailAppState.Loading
        repository.getMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    movie = it
                    appState.value = DetailAppState.MovieLoaded(it)
                },
                { appState.value = DetailAppState.LoadingError(it) })
            .also { disposables.add(it) }
    }

    override fun onCleared() {
        disposables.clear()
    }
}