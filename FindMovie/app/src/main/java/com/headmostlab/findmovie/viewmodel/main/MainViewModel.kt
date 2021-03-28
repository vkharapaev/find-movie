package com.headmostlab.findmovie.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.headmostlab.findmovie.Event
import com.headmostlab.findmovie.model.Repository
import com.headmostlab.findmovie.model.RepositoryImpl
import com.headmostlab.findmovie.model.ShortMovie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val appStateLiveData: MutableLiveData<MainAppState> = MutableLiveData(),
    private val disposables: CompositeDisposable = CompositeDisposable(),
    private val repository: Repository = RepositoryImpl(),
    private var shortMovies: List<ShortMovie>? = null
) :
    ViewModel() {

    fun getAppStateLiveData(): LiveData<MainAppState> {
        loadMovies()
        return appStateLiveData
    }

    private fun loadMovies() {
        val data = shortMovies;
        if (data != null) {
            appStateLiveData.value = MainAppState.MoviesLoaded(data)
        } else {
            appStateLiveData.value = MainAppState.Loading
            disposables.add(
                repository.getMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            appStateLiveData.postValue(MainAppState.MoviesLoaded(it))
                            shortMovies = it
                        },
                        { appStateLiveData.postValue(MainAppState.LoadingError(it)) }
                    )
            )
        }
    }

    fun clickMovieItem(position: Int) {
        val movie = shortMovies?.get(position)
        if (movie != null) {
            appStateLiveData.value = MainAppState.OnMovieItemClicked(Event(movie.id))
        }
    }

    override fun onCleared() {
        disposables.clear()
    }
}