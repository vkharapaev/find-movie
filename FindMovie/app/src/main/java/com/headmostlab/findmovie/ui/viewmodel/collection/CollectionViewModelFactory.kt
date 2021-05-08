package com.headmostlab.findmovie.ui.viewmodel.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.headmostlab.findmovie.data.datasource.local.RoomDb
import com.headmostlab.findmovie.data.repository.PagingRepository
import com.headmostlab.findmovie.data.repository.Repository

class CollectionViewModelFactory(
    private val collectionId: Int,
    private val repository: Repository,
    private val pagingRepository: PagingRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectionViewModel::class.java)) {
            return CollectionViewModel(collectionId, repository, pagingRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}