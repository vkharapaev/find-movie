package com.headmostlab.findmovie.view.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.databinding.DetailFragmentBinding
import com.headmostlab.findmovie.model.FullMovie
import com.headmostlab.findmovie.model.RepositoryImpl
import com.headmostlab.findmovie.model.apimodel.MovieDataSource
import com.headmostlab.findmovie.network.tmdb.TMDbApi
import com.headmostlab.findmovie.network.tmdb.TMDbApiKeyProvider
import com.headmostlab.findmovie.network.tmdb.TMDbHostProvider
import com.headmostlab.findmovie.viewmodel.detail.DetailAppState
import com.headmostlab.findmovie.viewmodel.detail.DetailViewModel
import com.headmostlab.findmovie.viewmodel.detail.DetailViewModelFactory

class DetailFragment : Fragment(R.layout.detail_fragment) {

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailViewModel
    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hostProvider = TMDbHostProvider()
        val apiKeyProvider = TMDbApiKeyProvider()
        val service = TMDbApi(hostProvider).getService()
        val dataSource = MovieDataSource(service, apiKeyProvider)
        val repository = RepositoryImpl(dataSource)

        viewModel = ViewModelProvider(this, DetailViewModelFactory(repository)).get(DetailViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = DetailFragmentBinding.bind(view)
        movieId = arguments?.getInt(PARAM_MOVIE_ID)
            ?: throw RuntimeException(getString(R.string.detail_fragment_movie_id_is_null))
        viewModel.getAppState(movieId).observe(viewLifecycleOwner, { renderState(it) })
    }

    private fun renderState(state: DetailAppState) {
        when (state) {
            DetailAppState.Loading -> {
                binding.loadingProgress.visibility = View.VISIBLE
                binding.content.visibility = View.INVISIBLE
            }
            is DetailAppState.MovieLoaded -> {
                binding.loadingProgress.visibility = View.INVISIBLE
                binding.content.visibility = View.VISIBLE
                setData(state.movie)
            }
            is DetailAppState.LoadingError -> {
                binding.loadingProgress.visibility = View.INVISIBLE
                Snackbar.make(
                    binding.main,
                    getString(R.string.error_message),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.button_reload)) { viewModel.getAppState(movieId) }
                    .show()
            }
        }
    }

    private fun setData(movie: FullMovie) {
        binding.movieId.text = movie.id.toString()
        binding.title.text = movie.title
        binding.origTitle.text = movie.origTitle
        binding.genres.text = movie.genres.toString()
        binding.duration.text = movie.duration.toString()
        binding.rating.text = movie.rating.toString()
        binding.budget.text = movie.budget.toString()
        binding.revenue.text = movie.revenue.toString()
        binding.year.text = movie.year.toString()
        binding.description.text = movie.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PARAM_MOVIE_ID = "MOVIE_ID"

        fun newInstance(movieId: Int): DetailFragment {
            val fragment = DetailFragment()
            fragment.arguments = Bundle().apply { putInt(PARAM_MOVIE_ID, movieId) }
            return fragment
        }
    }
}