package com.headmostlab.findmovie.ui.view.main

import androidx.lifecycle.LifecycleOwner
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.headmostlab.findmovie.databinding.MovieCategoryRowItemBinding
import com.headmostlab.findmovie.domain.entity.ShortMovie
import com.headmostlab.findmovie.ui.view.utils.addDivider
import com.headmostlab.findmovie.ui.viewmodel.main.model.UiMovieCollection
import com.rubensousa.recyclerview.ScrollStateHolder

class CategoryViewHolder(
    private val binding: MovieCategoryRowItemBinding,
    private val scrollState: ScrollStateHolder,
    private val listener: (ShortMovie) -> Unit,
    private val lifecycleOwner: LifecycleOwner,
    private val pagerErrorHandler: (Throwable) -> Unit
) :
    RecyclerView.ViewHolder(binding.root), ScrollStateHolder.ScrollStateKeyProvider {

    private var stateKey: String? = null
    private var data: UiMovieCollection? = null

    fun bind(item: UiMovieCollection) {
        data = item
        stateKey = item.title.toString()
        val adapter = MovieAdapter(
            listener,
            item.showSecondLayout
        )

        adapter.addLoadStateListener {
            if (it.append is LoadState.Error) {
                pagerErrorHandler((it.append as LoadState.Error).error)
            }
        }

        item.movies.observe(lifecycleOwner) {
            adapter.submitData(lifecycleOwner.lifecycle, it)
        }

        binding.recyclerView.adapter = adapter
        binding.title.text = binding.title.context.getText(item.title)
        scrollState.restoreScrollState(binding.recyclerView, this)
    }

    fun onRecycled() {
        data?.movies?.removeObservers(lifecycleOwner)
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
