package com.headmostlab.findmovie.ui.view.main

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.headmostlab.findmovie.DI
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.databinding.MainFragmentBinding
import com.headmostlab.findmovie.ui.view.nointernet.NoInternetFragment
import com.headmostlab.findmovie.ui.view.utils.addDivider
import com.headmostlab.findmovie.ui.view.utils.showSnackbar
import com.headmostlab.findmovie.ui.view.utils.viewBinding
import com.headmostlab.findmovie.ui.viewmodel.main.MainViewModel
import com.rubensousa.recyclerview.ScrollStateHolder
import java.io.IOException
import java.util.*

class MainFragment : Fragment(R.layout.main_fragment), ScrollStateHolder.ScrollStateKeyProvider {

    companion object {
        private const val MAIN_RECYCLER_VIEW = "MAIN_RECYCLER_VIEW"
    }

    private val component by lazy {
        DaggerMainScreenComponent.builder()
            .repository(DI.appComponent.repository())
            .pagingRepository(DI.appComponent.pagingRepository())
            .build()
    }

    private val viewModel by viewModels<MainViewModel> { component.viewModelFactory() }

    private val binding by viewBinding(MainFragmentBinding::bind)

    private var noInternet = false

    private lateinit var scrollStateHolder: ScrollStateHolder

    private lateinit var adapter: CollectionAdapter

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        scrollStateHolder.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scrollStateHolder = ScrollStateHolder(savedInstanceState)

        setFragmentResultListener(NoInternetFragment.RETRY_TO_CONNECT) { requestKey, bundle ->
            viewModel.loadMovies(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        noInternet = false

        ViewCompat.requestApplyInsets(view)

        setUpRecyclerView()

        observe()
    }

    private fun setUpRecyclerView() {
        adapter = createAdapter()
        with(binding.recyclerView) {
            adapter = this@MainFragment.adapter
            addDivider()
        }

        binding.headerLayout.title.text = getString(R.string.app_name)

        ViewCompat.setOnApplyWindowInsetsListener(binding.headerLayout.header) { _, inset ->
            val systemInsets = inset.getInsets(WindowInsetsCompat.Type.systemBars())
            val params = binding.headerLayout.header.layoutParams as FrameLayout.LayoutParams
            params.updateMargins(top = systemInsets.top)
            inset
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView) { _, inset ->
            val systemInsets = inset.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.recyclerView.updatePadding(
                left = systemInsets.left,
                right = systemInsets.right,
                top = systemInsets.top + resources.getDimensionPixelSize(
                    R.dimen.collection_recycler_view_top_padding
                ),
                bottom = systemInsets.bottom + resources.getDimensionPixelSize(R.dimen.bottom_padding)
            )
            inset
        }
        scrollStateHolder.setupRecyclerView(binding.recyclerView, this)
        scrollStateHolder.restoreScrollState(binding.recyclerView, this)
    }

    private fun observe() {
        viewModel.getCollections()
            .observe(viewLifecycleOwner, { adapter.movieCollection = it })
        viewModel.openMovieEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let { showDetail(it) }
        })
        viewModel.openCollectionEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let { showCollection(it) }
        })
    }

    private fun showCollection(collectionId: Int) {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToCollectionFragment(
                collectionId
            )
        )
    }

    private fun createAdapter() = CollectionAdapter(
        { viewModel.clickMovieItem(it) },
        { viewModel.selectCollection(it) },
        scrollStateHolder, viewLifecycleOwner
    ) {

        when (it) {
            is IOException -> {
                if (!noInternet) {
                    noInternet = true
                    findNavController().navigate(R.id.action_global_noInternetFragment)
                }
            }
            else ->
                binding.recyclerView.showSnackbar(
                    it.message ?: getString(R.string.error_message),
                    getString(R.string.button_reload)
                ) {
                    viewModel.loadMovies(true)
                }
        }
    }

    private fun showDetail(movieId: Int) {
        findNavController().navigate(MainFragmentDirections.actionGlobalDetailFragment(movieId))
    }

    override fun getScrollStateKey() = MAIN_RECYCLER_VIEW
}