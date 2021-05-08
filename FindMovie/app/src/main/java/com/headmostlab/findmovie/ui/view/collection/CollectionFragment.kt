package com.headmostlab.findmovie.ui.view.collection

import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.headmostlab.findmovie.App
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.data.datasource.network.TMDbDataSource
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApi
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApiKeyProvider
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbHostProvider
import com.headmostlab.findmovie.data.repository.PagingRepositoryImpl
import com.headmostlab.findmovie.data.repository.RepositoryImpl
import com.headmostlab.findmovie.databinding.CollectionFragmentBinding
import com.headmostlab.findmovie.ui.view.detail.DetailFragment
import com.headmostlab.findmovie.ui.view.utils.addLargeDivider
import com.headmostlab.findmovie.ui.view.utils.viewBinding
import com.headmostlab.findmovie.ui.viewmodel.collection.CollectionViewModel
import com.headmostlab.findmovie.ui.viewmodel.collection.CollectionViewModelFactory

class CollectionFragment : Fragment(R.layout.collection_fragment) {

    private val binding by viewBinding(CollectionFragmentBinding::bind)

    private val collectionId: Int by lazy {
        arguments?.getInt(PARAM_COLLECTION_ID)
            ?: throw IllegalArgumentException("Collection id must be provided")
    }

    private lateinit var adapter: CollectionAdapter

    private val viewModel: CollectionViewModel by lazy {
        val service = TMDbApi(TMDbHostProvider()).getService()
        val dataSource = TMDbDataSource(service, TMDbApiKeyProvider())
        val db = App.instance.database
        val repository = RepositoryImpl(dataSource, db)
        val pagingRepository = PagingRepositoryImpl(service, db)
        ViewModelProvider(
            this,
            CollectionViewModelFactory(collectionId, repository, pagingRepository)
        ).get(CollectionViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CollectionAdapter {
            viewModel.clickMovieItem(it)
        }

        with(binding.recyclerView) {
            adapter = this@CollectionFragment.adapter
            addLargeDivider()
        }

        viewModel.movies.observe(viewLifecycleOwner, {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })

        viewModel.openMovieEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let { showDetail(it) }
        })

        viewModel.collection.observe(viewLifecycleOwner, {
            binding.title.text = getString(it.eCollection.title)
        })

        ViewCompat.setOnApplyWindowInsetsListener(binding.header) { _, inset ->
            val systemInsets = inset.getInsets(WindowInsetsCompat.Type.systemBars())
            val params = binding.header.layoutParams as CoordinatorLayout.LayoutParams
            params.updateMargins(top = systemInsets.top)
            inset
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView) { _, inset ->
            val systemInsets = inset.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.recyclerView.updatePadding(
                top = systemInsets.top + resources.getDimensionPixelSize(
                    R.dimen.collection_recycler_view_top_padding
                ),
                bottom = systemInsets.bottom + resources.getDimensionPixelSize(R.dimen.recycler_view_bottom_padding)
            )
            inset
        }
    }

    private fun showDetail(movieId: Int) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.container, DetailFragment.newInstance(movieId))
            addToBackStack("")
            commit()
        }
    }

    companion object {
        private const val PARAM_COLLECTION_ID = "COLLECTION_ID"
        fun newInstance(collectionId: Int) = CollectionFragment().apply {
            arguments = bundleOf(PARAM_COLLECTION_ID to collectionId)
        }
    }
}