package com.headmostlab.findmovie.ui.viewmodel.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.headmostlab.findmovie.data.repository.Repository
import com.headmostlab.findmovie.data.repository.VideoRepository

class DetailViewModelFactory(private val repository: Repository, private val  videoRepository: VideoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository, videoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}