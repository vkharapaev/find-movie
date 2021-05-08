package com.headmostlab.findmovie.ui.view.collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.domain.entity.ShortMovie

class CollectionAdapter(
    private val movieClickListener: (ShortMovie) -> Unit
) : PagingDataAdapter<ShortMovie, NarrowMovieViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NarrowMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.narrow_movie_row_item_in_grid, parent, false)
        return NarrowMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: NarrowMovieViewHolder, position: Int) {
        holder.bind(movieClickListener, getItem(position))
    }

    override fun onViewRecycled(holder: NarrowMovieViewHolder) {
        super.onViewRecycled(holder)
        holder.onRecycled()
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ShortMovie>() {
            override fun areItemsTheSame(oldItem: ShortMovie, newItem: ShortMovie): Boolean {
                return oldItem.id == oldItem.id
            }

            override fun areContentsTheSame(oldItem: ShortMovie, newItem: ShortMovie) =
                oldItem == newItem
        }
    }
}