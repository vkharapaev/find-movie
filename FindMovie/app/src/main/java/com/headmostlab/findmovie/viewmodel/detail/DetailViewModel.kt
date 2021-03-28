package com.headmostlab.findmovie.viewmodel.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.headmostlab.findmovie.model.FullMovie
import com.headmostlab.findmovie.model.Repository
import com.headmostlab.findmovie.model.RepositoryImpl
import java.util.concurrent.TimeUnit

class DetailViewModel(
    private val repository: Repository = RepositoryImpl(),
    private val appState: MutableLiveData<DetailAppState> = MutableLiveData(),
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
        Thread {
            TimeUnit.SECONDS.sleep(1)
            val movie = repository.getMovie(movieId)
            this.movie = movie
            appState.postValue(DetailAppState.MovieLoaded(movie))
        }.start()
    }
}