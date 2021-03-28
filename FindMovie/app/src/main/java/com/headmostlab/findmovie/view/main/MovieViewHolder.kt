package com.headmostlab.findmovie.view.main

import androidx.recyclerview.widget.RecyclerView
import com.headmostlab.findmovie.databinding.MovieRowItemBinding
import com.headmostlab.findmovie.model.Movie

class MovieViewHolder(private val binding: MovieRowItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(listener: MainFragment.OnItemClickedListener, item: Movie) {
        binding.movieId.text = item.id.toString()
        binding.title.text = item.title
        binding.year.text = item.year.toString()
        binding.rating.text = item.rating.toString()
        binding.poster.text = item.poster
        binding.root.setOnClickListener { listener.clicked(adapterPosition) }
    }

}
