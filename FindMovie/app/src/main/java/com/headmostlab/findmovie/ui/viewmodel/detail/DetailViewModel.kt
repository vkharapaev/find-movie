package com.headmostlab.findmovie.ui.viewmodel.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.data.repository.Repository
import com.headmostlab.findmovie.data.repository.VideoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val repository: Repository,
    private val videoRepository: VideoRepository
) : ViewModel() {

    private val state: MutableLiveData<State> = MutableLiveData()
    private val disposables: CompositeDisposable = CompositeDisposable()
    private var movie: FullMovie? = null

    private val _videoUrlMutableLiveData = MutableLiveData<String>()
    val videoUrlLiveData: LiveData<String> = _videoUrlMutableLiveData

    private fun getVideoUrl(videoId: String) {
        videoRepository.getVideoUrl(videoId).subscribe({ url ->
            _videoUrlMutableLiveData.value = url
        }, {

        }).also { disposables.add(it) }
    }

    fun getVideoUrl(movieId: Int) {
        repository.getVideos(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isNotEmpty()) {
                    getVideoUrl(it.first())
                }
            }, {

            })
            .also { disposables.add(it) }
    }

    fun getAppState(movieId: Int): LiveData<State> = state.also { loadMovie(movieId) }

    private fun loadMovie(movieId: Int) {
        movie?.let {
            state.value = State.Success(it)
            return
        }
        state.value = State.Loading
        repository.getMovie(movieId)
            .zipWith(repository.getPeople(movieId), { movie, actors ->
                movie.also { it.people = actors }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    movie = it
                    state.value = State.Success(it)
                },
                { state.value = State.Error(it) })
            .also { disposables.add(it) }
    }

    override fun onCleared() {
        disposables.clear()
    }
}