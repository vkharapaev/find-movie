package com.headmostlab.findmovie.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.databinding.MainFragmentBinding
import com.headmostlab.findmovie.data.repository.RepositoryImpl
import com.headmostlab.findmovie.data.datasource.network.TMDbDataSource
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApi
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApiKeyProvider
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbHostProvider
import com.headmostlab.findmovie.data.repository.MockRepository
import com.headmostlab.findmovie.ui.view.utils.showSnackbar
import com.headmostlab.findmovie.ui.view.detail.DetailFragment
import com.headmostlab.findmovie.ui.view.utils.addDivider
import com.headmostlab.findmovie.ui.viewmodel.main.MainAppState
import com.headmostlab.findmovie.ui.viewmodel.main.MainViewModel
import com.headmostlab.findmovie.ui.viewmodel.main.MainViewModelFactory

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null

    private val binding get() = _binding!!

    private val storedAdapter by lazy {
        CategoryAdapter(object : OnItemClickedListener {
            override fun clicked(categoryPosition: Int, moviePosition: Int) {
                viewModel.clickMovieItem(categoryPosition, moviePosition)
            }
        })
    }

    private val viewModel: MainViewModel by lazy {
        val service = TMDbApi(TMDbHostProvider()).getService()
        val dataSource = TMDbDataSource(service, TMDbApiKeyProvider())
        val repository = MockRepository() //RepositoryImpl(dataSource)
        ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding.recyclerView) {
            adapter = storedAdapter
            addDivider()
        }
        viewModel.getAppStateLiveData().observe(viewLifecycleOwner, { renderAppState(it) })
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
            is MainAppState.MoviesLoaded -> {
                storedAdapter.movieCategories = state.movies
                binding.apply {
                    loadingProgress.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                }
            }
            is MainAppState.LoadingError -> binding.apply {
                loadingProgress.visibility = View.INVISIBLE
                main.showSnackbar(R.string.error_message, R.string.button_reload) {
                    viewModel.getAppStateLiveData()
                }
            }
            is MainAppState.OnMovieItemClicked -> {
                binding.apply {
                    loadingProgress.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                }
                state.movieId.getContentIfNotHandled()?.let { showDetail(it) }
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

    interface OnItemClickedListener {
        fun clicked(categoryPosition: Int, moviePosition: Int)
    }

}