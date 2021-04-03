package com.headmostlab.findmovie.view.main

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
import com.headmostlab.findmovie.model.RepositoryImpl
import com.headmostlab.findmovie.model.apimodel.MovieDataSource
import com.headmostlab.findmovie.network.tmdb.TMDbApi
import com.headmostlab.findmovie.network.tmdb.TMDbApiKeyProvider
import com.headmostlab.findmovie.network.tmdb.TMDbHostProvider
import com.headmostlab.findmovie.utils.showSnackbar
import com.headmostlab.findmovie.view.detail.DetailFragment
import com.headmostlab.findmovie.viewmodel.main.MainAppState
import com.headmostlab.findmovie.viewmodel.main.MainViewModel
import com.headmostlab.findmovie.viewmodel.main.MainViewModelFactory

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private var adapter = MovieAdapter(object : OnItemClickedListener {
        override fun clicked(position: Int) {
            viewModel.clickMovieItem(position)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = TMDbApi(TMDbHostProvider()).getService()
        val dataSource = MovieDataSource(service, TMDbApiKeyProvider())
        val repository = RepositoryImpl(dataSource)

        viewModel = ViewModelProvider(
            this, MainViewModelFactory(repository)
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(createDividerDecoration())
        viewModel.getAppStateLiveData().observe(viewLifecycleOwner, { renderAppState(it) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderAppState(state: MainAppState): Unit {
        when (state) {
            MainAppState.Loading -> {
                binding.loadingProgress.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.INVISIBLE
            }
            is MainAppState.MoviesLoaded -> {
                adapter.submitList(state.movies)
                binding.loadingProgress.visibility = View.INVISIBLE
                binding.recyclerView.visibility = View.VISIBLE
            }
            is MainAppState.LoadingError -> {
                binding.loadingProgress.visibility = View.INVISIBLE
                binding.main.showSnackbar(R.string.error_message, R.string.button_reload) {
                    viewModel.getAppStateLiveData()
                }
            }
            is MainAppState.OnMovieItemClicked -> {
                binding.loadingProgress.visibility = View.INVISIBLE
                binding.recyclerView.visibility = View.VISIBLE
                val movieId = state.movieId.getContentIfNotHandled()
                movieId?.let { showDetail(it) }
            }
        }
    }

    private fun showDetail(movieId: Int) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, DetailFragment.newInstance(movieId))
            .addToBackStack("")
            .commit()
    }

    private fun createDividerDecoration(): DividerItemDecoration {
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.divider, null)
        drawable?.apply { decoration.setDrawable(drawable) }
        return decoration
    }

    interface OnItemClickedListener {
        fun clicked(position: Int)
    }

}