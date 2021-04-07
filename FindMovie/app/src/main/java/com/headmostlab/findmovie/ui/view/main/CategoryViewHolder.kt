package com.headmostlab.findmovie.ui.view.main

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.headmostlab.findmovie.databinding.MovieCategoryRowItemBinding
import com.headmostlab.findmovie.domain.entity.MovieWithCategory
import com.headmostlab.findmovie.ui.view.utils.addDivider
import com.rubensousa.recyclerview.ScrollStateHolder

class CategoryViewHolder(
    private val binding: MovieCategoryRowItemBinding,
    private val scrollState: ScrollStateHolder,
    private val listener: MainFragment.OnItemClickedListener
) :
    RecyclerView.ViewHolder(binding.root), ScrollStateHolder.ScrollStateKeyProvider {

    private var stateKey: String? = null

    fun bind(item: MovieWithCategory) {
        stateKey = item.category.name
        val adapter = MovieAdapter(adapterPosition, listener, item.category).apply {
            categoryPosition = adapterPosition
            submitList(item.movies)
        }
        binding.recyclerView.adapter = adapter
        binding.title.text = binding.title.context.getText(item.category.title)
        scrollState.restoreScrollState(binding.recyclerView, this)
    }

    fun onRecycled() {
        scrollState.saveScrollState(binding.recyclerView, this)
    }

    override fun getScrollStateKey(): String? {
        return stateKey
    }

    init {
        binding.recyclerView.addDivider(DividerItemDecoration.HORIZONTAL)
        scrollState.setupRecyclerView(binding.recyclerView, this)
    }
}
