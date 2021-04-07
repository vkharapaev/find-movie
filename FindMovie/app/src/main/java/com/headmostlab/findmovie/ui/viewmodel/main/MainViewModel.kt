package com.headmostlab.findmovie.ui.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.headmostlab.findmovie.Event
import com.headmostlab.findmovie.data.repository.Repository
import com.headmostlab.findmovie.domain.entity.ShortMovie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val repository: Repository,
    private val appStateLiveData: MutableLiveData<MainAppState> = MutableLiveData(),
    private val disposables: CompositeDisposable = CompositeDisposable(),
    private var movies: List<ShortMovie>? = null
) :
    ViewModel() {

    fun getAppStateLiveData(): LiveData<MainAppState> = appStateLiveData.also { loadMovies() }

    private fun loadMovies() {
        val data = movies;
        if (data != null) {
            appStateLiveData.value = MainAppState.MoviesLoaded(data)
        } else {
            appStateLiveData.value = MainAppState.Loading
            repository.getMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        movies = it
                        appStateLiveData.postValue(MainAppState.MoviesLoaded(it))
                    },
                    { appStateLiveData.postValue(MainAppState.LoadingError(it)) }
                )
                .also { disposables.add(it) }
        }
    }

    fun clickMovieItem(position: Int) {
        movies?.get(position)?.let { movie ->
            appStateLiveData.value = MainAppState.OnMovieItemClicked(Event(movie.id))
        }
    }

    override fun onCleared() {
        disposables.clear()
    }
}