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
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.MovieDataSource
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApi
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApiKeyProvider
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbHostProvider
import com.headmostlab.findmovie.ui.view.utils.showSnackbar
import com.headmostlab.findmovie.ui.view.detail.DetailFragment
import com.headmostlab.findmovie.ui.viewmodel.main.MainAppState
import com.headmostlab.findmovie.ui.viewmodel.main.MainViewModel
import com.headmostlab.findmovie.ui.viewmodel.main.MainViewModelFactory

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null

    private val binding get() = _binding!!

    private var storedAdapter = MovieAdapter(object : OnItemClickedListener {
        override fun clicked(position: Int) {
            viewModel.clickMovieItem(position)
        }
    })

    private val viewModel: MainViewModel by lazy {
        val service = TMDbApi(TMDbHostProvider()).getService()
        val dataSource = MovieDataSource(service, TMDbApiKeyProvider())
        val repository = RepositoryImpl(dataSource)
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
            addItemDecoration(createDividerDecoration())
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
                storedAdapter.submitList(state.movies)
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

    private fun createDividerDecoration(): DividerItemDecoration {
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.divider, null)
        val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        drawable?.let { divider.setDrawable(it) }
        return divider
    }

    interface OnItemClickedListener {
        fun clicked(position: Int)
    }

}