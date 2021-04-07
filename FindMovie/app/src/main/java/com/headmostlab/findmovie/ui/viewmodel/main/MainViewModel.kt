package com.headmostlab.findmovie.ui.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.headmostlab.findmovie.Event
import com.headmostlab.findmovie.data.repository.Repository
import com.headmostlab.findmovie.domain.entity.MovieCategory
import com.headmostlab.findmovie.domain.entity.MovieWithCategory
import com.headmostlab.findmovie.domain.entity.ShortMovie
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val repository: Repository,
    private val appStateLiveData: MutableLiveData<MainAppState> = MutableLiveData(),
    private val disposables: CompositeDisposable = CompositeDisposable(),
    private val moviesWithCategories: ArrayList<MovieWithCategory> = ArrayList()
) :
    ViewModel() {

    fun getAppStateLiveData(): LiveData<MainAppState> = appStateLiveData.also { loadMovies() }

    private fun loadMovies(reload: Boolean = false) {
        if (!reload) {
            appStateLiveData.value = if (moviesWithCategories.isEmpty()) {
                MainAppState.Loading
            } else {
                MainAppState.MoviesLoaded(moviesWithCategories)
            }
        }

        if (reload || disposables.size() == 0) {
            moviesWithCategories.clear()
            appStateLiveData.value = MainAppState.Loading
            Observable.fromIterable(repository.getMovieCategories())
                    .flatMap { getMovies(it).toObservable() }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ item ->
                        moviesWithCategories.add(item)
                        appStateLiveData.value =
                                MainAppState.MoviesLoaded(moviesWithCategories.toList())
                    }, { appStateLiveData.postValue(MainAppState.LoadingError(it)) })
                    .also { disposables.add(it) }
        }
    }

    private fun getMovies(category: MovieCategory) = when (category) {
        MovieCategory.POPULAR -> repository.getMovies()
        MovieCategory.NOW_PLAYING -> TODO()
        MovieCategory.UPCOMING -> TODO()
    }.map { movies -> MovieWithCategory(category, movies) }

    fun clickMovieItem(categoryPosition: Int, moviePosition: Int) {
        val moviesWithCategory = moviesWithCategories[categoryPosition]
        appStateLiveData.value =
                MainAppState.OnMovieItemClicked(Event(moviesWithCategory.movies[moviePosition].id))
    }

    override fun onCleared() {
        disposables.clear()
    }
}