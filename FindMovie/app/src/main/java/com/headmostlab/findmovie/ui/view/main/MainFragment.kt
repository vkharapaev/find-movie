package com.headmostlab.findmovie.ui.view.main

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.headmostlab.findmovie.App
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.data.datasource.network.TMDbDataSource
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApi
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApiKeyProvider
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbHostProvider
import com.headmostlab.findmovie.data.repository.PagingRepositoryImpl
import com.headmostlab.findmovie.data.repository.RepositoryImpl
import com.headmostlab.findmovie.databinding.MainFragmentBinding
import com.headmostlab.findmovie.ui.view.detail.DetailFragment
import com.headmostlab.findmovie.ui.view.nointernet.NoInternetFragment
import com.headmostlab.findmovie.ui.view.utils.addDivider
import com.headmostlab.findmovie.ui.view.utils.showSnackbar
import com.headmostlab.findmovie.ui.view.utils.viewBinding
import com.headmostlab.findmovie.ui.viewmodel.main.MainViewModel
import com.headmostlab.findmovie.ui.viewmodel.main.MainViewModelFactory
import com.rubensousa.recyclerview.ScrollStateHolder
import java.io.IOException
import java.util.*

class MainFragment : Fragment(R.layout.main_fragment), ScrollStateHolder.ScrollStateKeyProvider {

    companion object {
        fun newInstance() = MainFragment()
        private const val MAIN_RECYCLER_VIEW = "MAIN_RECYCLER_VIEW"
    }

    private val binding by viewBinding(MainFragmentBinding::bind)

    private lateinit var scrollStateHolder: ScrollStateHolder

    private lateinit var adapter: CollectionAdapter

    private val viewModel: MainViewModel by lazy {
        val service = TMDbApi(TMDbHostProvider()).getService()
        val dataSource = TMDbDataSource(service, TMDbApiKeyProvider())
        val db = App.instance.database
        val repository = RepositoryImpl(dataSource, db)
        val pagingRepository = PagingRepositoryImpl(service, db)
        ViewModelProvider(this, MainViewModelFactory(repository, pagingRepository)).get(
            MainViewModel::class.java
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        scrollStateHolder.onSaveInstanceState(outState)
    }

    private var noInternet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scrollStateHolder = ScrollStateHolder(savedInstanceState)

        setFragmentResultListener("RETRY_TO_CONNECT") { requestKey, bundle ->
            viewModel.loadMovies(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        noInternet = false
        adapter = createAdapter()

        with(binding.recyclerView) {
            adapter = this@MainFragment.adapter
            addDivider()
        }
        viewModel.getAppStateLiveData()
            .observe(viewLifecycleOwner, { adapter.movieCollection = it })
        scrollStateHolder.setupRecyclerView(binding.recyclerView, this)
        scrollStateHolder.restoreScrollState(binding.recyclerView, this)
        viewModel.openMovieEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let { showDetail(it) }
        })

        ViewCompat.requestApplyInsets(view)

        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView) { _, inset ->
            val systemInsets = inset.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.recyclerView.updatePadding(
                left = systemInsets.left,
                right = systemInsets.right,
                top = systemInsets.top + resources.getDimensionPixelSize(R.dimen.top_margin),
                bottom = systemInsets.bottom + resources.getDimensionPixelSize(R.dimen.bottom_margin)
            )
            inset
        }
    }

    private fun createAdapter() = CollectionAdapter(
        { viewModel.clickMovieItem(it) },
        scrollStateHolder, viewLifecycleOwner
    ) {

        when (it) {
            is IOException -> {
                if (!noInternet) {
                    noInternet = true
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.container, NoInternetFragment.newInstance())
                        addToBackStack(null)
                    }.commit()
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
        requireActivity().supportFragmentManager.apply {
            beginTransaction()
                .replace(R.id.container, DetailFragment.newInstance(movieId))
                .addToBackStack("")
                .commit()
        }
    }

    override fun getScrollStateKey() = MAIN_RECYCLER_VIEW
}