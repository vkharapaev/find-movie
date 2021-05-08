package com.headmostlab.findmovie.ui.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.domain.entity.ShortMovie

class MovieAdapter(
    private val listener: (ShortMovie) -> Unit,
    private val secondLayout: Boolean
) :
    PagingDataAdapter<ShortMovie, MovieViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.narrow_movie_row_item -> NarrowMovieViewHolder(view)
            else -> WideMovieViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(listener, getItem(position))
    }

    override fun getItemViewType(position: Int) = when (secondLayout) {
        false -> R.layout.narrow_movie_row_item
        true -> R.layout.wide_movie_row_item
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
