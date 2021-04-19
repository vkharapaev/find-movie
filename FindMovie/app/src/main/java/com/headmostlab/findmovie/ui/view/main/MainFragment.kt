package com.headmostlab.findmovie.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.headmostlab.findmovie.databinding.MainFragmentBinding
import com.headmostlab.findmovie.ui.view.detail.DetailFragment
import com.headmostlab.findmovie.ui.view.utils.addDivider
import com.headmostlab.findmovie.ui.view.utils.showSnackbar
import com.headmostlab.findmovie.ui.viewmodel.main.MainAppState
import com.headmostlab.findmovie.ui.viewmodel.main.MainViewModel
import com.headmostlab.findmovie.ui.viewmodel.main.MainViewModelFactory
import com.rubensousa.recyclerview.ScrollStateHolder

class MainFragment : Fragment(), ScrollStateHolder.ScrollStateKeyProvider {

    companion object {
        fun newInstance() = MainFragment()
        private const val MAIN_RECYCLER_VIEW = "MAIN_RECYCLER_VIEW"
    }

    private var _binding: MainFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var scrollStateHolder: ScrollStateHolder

    private lateinit var adapter: CategoryAdapter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scrollStateHolder = ScrollStateHolder(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = createAdapter()
        with(binding.recyclerView) {
            adapter = this@MainFragment.adapter
            addDivider()
        }
        viewModel.getAppStateLiveData().observe(viewLifecycleOwner, { renderAppState(it) })
        scrollStateHolder.setupRecyclerView(binding.recyclerView, this)
        scrollStateHolder.restoreScrollState(binding.recyclerView, this)
        viewModel.openMovieEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let { showDetail(it) }
        })
    }

    private fun createAdapter() = CategoryAdapter(
        { viewModel.clickMovieItem(it) },
        scrollStateHolder, viewLifecycleOwner
    ) {
        binding.recyclerView.showSnackbar(R.string.error_message, R.string.button_reload) {
            viewModel.getAppStateLiveData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderAppState(state: MainAppState) {
        when (state) {
            MainAppState.Loading -> binding.apply {
                loadingProgress.visibility = View.VISIBLE
                recyclerView.visibility = View.INVISIBLE
            }
            is MainAppState.Loaded -> {
                adapter.movieCollection = state.movies
                binding.apply {
                    loadingProgress.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                }
            }
            is MainAppState.Error -> binding.apply {
                loadingProgress.visibility = View.INVISIBLE
                main.showSnackbar(R.string.error_message, R.string.button_reload) {
                    viewModel.getAppStateLiveData()
                }
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