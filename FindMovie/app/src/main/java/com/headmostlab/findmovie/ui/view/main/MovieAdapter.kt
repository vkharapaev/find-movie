package com.headmostlab.findmovie.ui.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.headmostlab.findmovie.databinding.MovieRowItemBinding
import com.headmostlab.findmovie.domain.entity.ShortMovie

class MovieAdapter(private val listener: MainFragment.OnItemClickedListener) :
    ListAdapter<ShortMovie, MovieViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        MovieRowItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(listener, getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ShortMovie>() {
            override fun areItemsTheSame(oldItem: ShortMovie, newItem: ShortMovie) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ShortMovie, newItem: ShortMovie) =
                oldItem == newItem
        }
    }
}
