package com.headmostlab.findmovie.ui.view.main

import androidx.recyclerview.widget.RecyclerView
import com.headmostlab.findmovie.databinding.MovieRowItemBinding
import com.headmostlab.findmovie.domain.entity.ShortMovie

class MovieViewHolder(private val binding: MovieRowItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(listener: MainFragment.OnItemClickedListener, item: ShortMovie) {
        with(binding) {
            movieId.text = item.id.toString()
            title.text = item.title
            year.text = item.year.toString()
            rating.text = item.rating.toString()
            poster.text = item.poster
            root.setOnClickListener { listener.clicked(adapterPosition) }
        }
    }
}
