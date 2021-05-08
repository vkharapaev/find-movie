package com.headmostlab.findmovie.ui.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.domain.entity.Collection
import com.headmostlab.findmovie.domain.entity.ShortMovie
import com.headmostlab.findmovie.ui.viewmodel.main.model.UiItem
import java.lang.IllegalArgumentException

class MovieAdapter(
    private val listener: (ShortMovie) -> Unit,
    private val showCollectionListener: (Collection) -> Unit,
    private val secondLayout: Boolean
) :
    PagingDataAdapter<UiItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.narrow_movie_row_item -> NarrowMovieViewHolder(view)
            R.layout.wide_movie_row_item -> WideMovieViewHolder(view)
            R.layout.full_collection_row_item -> FullCategoryViewHolder(view)
            else -> throw IllegalArgumentException("Not supported view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> {
                val item = getItem(position) as UiItem.Movie
                holder.bind(listener, item.movie)
            }
            is FullCategoryViewHolder -> {
                val item = getItem(position) as UiItem.Footer
                holder.bind(item.collection, showCollectionListener)
            }
            else -> throw IllegalArgumentException("Not supported view holder")
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is UiItem.Movie -> when (secondLayout) {
            true -> R.layout.wide_movie_row_item
            false -> R.layout.narrow_movie_row_item
        }
        else -> R.layout.full_collection_row_item
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is MovieViewHolder) {
            holder.onRecycled()
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UiItem>() {
            override fun areItemsTheSame(oldItem: UiItem, newItem: UiItem): Boolean {
                val isSameRepoItem = oldItem is UiItem.Movie && newItem is UiItem.Movie &&
                        oldItem.movie.id == newItem.movie.id

                val isSameFooterItem = oldItem is UiItem.Footer && newItem is UiItem.Footer &&
                        oldItem.collection.id == newItem.collection.id

                return isSameRepoItem || isSameFooterItem
            }

            override fun areContentsTheSame(oldItem: UiItem, newItem: UiItem) =
                oldItem == newItem
        }
    }
}
