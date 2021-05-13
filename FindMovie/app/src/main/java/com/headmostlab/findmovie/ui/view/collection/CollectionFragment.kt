package com.headmostlab.findmovie.ui.view.collection

import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.headmostlab.findmovie.DI
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.databinding.CollectionFragmentBinding
import com.headmostlab.findmovie.ui.view.nointernet.NoInternetFragment
import com.headmostlab.findmovie.ui.view.utils.addLargeDivider
import com.headmostlab.findmovie.ui.view.utils.viewBinding
import com.headmostlab.findmovie.ui.viewmodel.collection.CollectionViewModel
import java.io.IOException

class CollectionFragment : Fragment(R.layout.collection_fragment) {

    private val args: CollectionFragmentArgs by navArgs()

    private val component by lazy {
        DaggerCollectionScreenComponent.builder()
            .collectionId(args.collectionId)
            .repository(DI.appComponent.repository())
            .pagingRepository(DI.appComponent.pagingRepository())
            .build()
    }

    private val viewModel by viewModels<CollectionViewModel> { component.viewModelFactory() }

    private val binding by viewBinding(CollectionFragmentBinding::bind)

    private lateinit var adapter: CollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(NoInternetFragment.RETRY_TO_CONNECT) { requestKey, bundle -> }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CollectionAdapter { viewModel.clickMovieItem(it) }

        adapter.addLoadStateListener {
            if (it.append is LoadState.Error) {
                when ((it.append as LoadState.Error).error) {
                    is IOException -> {
                        findNavController().navigate(CollectionFragmentDirections.actionGlobalNoInternetFragment())
                    }
                }
            }
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
            binding.headerLayout.title.text = getString(it.eCollection.title)
        })

        ViewCompat.setOnApplyWindowInsetsListener(binding.headerLayout.header) { _, inset ->
            val systemInsets = inset.getInsets(WindowInsetsCompat.Type.systemBars())
            val params = binding.headerLayout.header.layoutParams as CoordinatorLayout.LayoutParams
            params.updateMargins(top = systemInsets.top)
            inset
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView) { _, inset ->
            val systemInsets = inset.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.recyclerView.updatePadding(
                top = systemInsets.top + resources.getDimensionPixelSize(
                    R.dimen.collection_recycler_view_top_padding
                ),
                bottom = systemInsets.bottom + resources.getDimensionPixelSize(R.dimen.bottom_padding)
            )
            inset
        }
    }

    private fun showDetail(movieId: Int) {
        findNavController().navigate(CollectionFragmentDirections.actionGlobalDetailFragment(movieId))
    }
}