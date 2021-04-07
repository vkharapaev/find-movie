package com.headmostlab.findmovie.ui.view.main

import androidx.recyclerview.widget.RecyclerView
import com.headmostlab.findmovie.databinding.MovieCategoryRowItemBinding
import com.headmostlab.findmovie.domain.entity.MovieWithCategory

class CategoryViewHolder(
    private val binding: MovieCategoryRowItemBinding,
    private val listener: MainFragment.OnItemClickedListener
) :
    RecyclerView.ViewHolder(binding.root) {

    private var stateKey: String? = null

    fun bind(item: MovieWithCategory) {
        stateKey = item.category.name
        val adapter = MovieAdapter(adapterPosition, listener, item.category).apply {
            categoryPosition = adapterPosition
            submitList(item.movies)
        }
        binding.recyclerView.adapter = adapter
        binding.title.text = binding.title.context.getText(item.category.title)
    }
}
