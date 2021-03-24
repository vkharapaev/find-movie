package com.headmostlab.findmovie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.headmostlab.findmovie.model.Repository
import com.headmostlab.findmovie.model.RepositoryImpl
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val appStateLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: Repository = RepositoryImpl()
) :
    ViewModel() {

    fun getAppStateLiveData(): LiveData<AppState> = appStateLiveData

    fun getMovies() {
        appStateLiveData.value = AppState.Loading
        Thread {
            TimeUnit.SECONDS.sleep(1)
            appStateLiveData.postValue(AppState.Success(repository.getMovies()))
        }.start()
    }
}