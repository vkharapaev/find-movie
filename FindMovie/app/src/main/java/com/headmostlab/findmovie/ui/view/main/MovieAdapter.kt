package com.headmostlab.findmovie.ui.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.databinding.MovieRowItemBinding
import com.headmostlab.findmovie.domain.entity.MovieCategory
import com.headmostlab.findmovie.domain.entity.ShortMovie

class MovieAdapter(
        var categoryPosition: Int,
        private val listener: MainFragment.OnItemClickedListener,
        private val category: MovieCategory
) :
        ListAdapter<ShortMovie, MovieViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.movie_row_item -> MovieViewHolderImpl(view)
            R.layout.movie_row_item2 -> MovieViewHolder2Impl(view)
            else -> MovieViewHolderImpl(view)
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(listener, categoryPosition, getItem(position))
    }

    override fun getItemViewType(position: Int) =
            when (category) {
                MovieCategory.NOW_PLAYING -> R.layout.movie_row_item
                MovieCategory.UPCOMING -> R.layout.movie_row_item2
                MovieCategory.POPULAR -> R.layout.movie_row_item
            }

    override fun onViewRecycled(holder: MovieViewHolder) {
        super.onViewRecycled(holder)
        holder.onRecycled()
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ShortMovie>() {
            override fun areItemsTheSame(oldItem: ShortMovie, newItem: ShortMovie) =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ShortMovie, newItem: ShortMovie) =
                    oldItem == newItem
        }
    }

}
