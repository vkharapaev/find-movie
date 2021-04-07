package com.headmostlab.findmovie.ui.view.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.headmostlab.findmovie.GlideApp
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.databinding.DetailFragmentBinding
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.data.repository.RepositoryImpl
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.MovieDataSource
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApi
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApiKeyProvider
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbHostProvider
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbImageHostProvider
import com.headmostlab.findmovie.ui.view.utils.showSnackbar
import com.headmostlab.findmovie.ui.viewmodel.detail.DetailAppState
import com.headmostlab.findmovie.ui.viewmodel.detail.DetailViewModel
import com.headmostlab.findmovie.ui.viewmodel.detail.DetailViewModelFactory

class DetailFragment : Fragment(R.layout.detail_fragment) {

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!
    private var storedMovieId: Int = 0

    private val viewModel: DetailViewModel by lazy {
        val service = TMDbApi(TMDbHostProvider()).getService()
        val dataSource = MovieDataSource(service, TMDbApiKeyProvider())
        val repository = RepositoryImpl(dataSource)
        ViewModelProvider(this, DetailViewModelFactory(repository)).get(DetailViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = DetailFragmentBinding.bind(view)
        storedMovieId = arguments?.getInt(PARAM_MOVIE_ID)
            ?: throw RuntimeException(getString(R.string.detail_fragment_movie_id_is_null))
        viewModel.getAppState(storedMovieId).observe(viewLifecycleOwner, { renderState(it) })
    }

    private fun renderState(state: DetailAppState) {
        when (state) {
            DetailAppState.Loading -> binding.apply {
                loadingProgress.visibility = View.VISIBLE
                binding.content.visibility = View.INVISIBLE
            }
            is DetailAppState.MovieLoaded -> {
                binding.apply {
                    loadingProgress.visibility = View.INVISIBLE
                    content.visibility = View.VISIBLE
                }
                setData(state.movie)
            }
            is DetailAppState.LoadingError -> {
                binding.apply {
                    loadingProgress.visibility = View.INVISIBLE
                    main.showSnackbar(R.string.error_message, R.string.button_reload) {
                        viewModel.getAppState(storedMovieId)
                    }
                }
            }
        }
    }

    private fun setData(movie: FullMovie) {
        with(binding) {
            movieId.text = movie.id.toString()
            title.text = movie.title
            origTitle.text = movie.origTitle
            genres.text = movie.genres.toString()
            duration.text = movie.duration.toString()
            rating.text = movie.rating.toString()
            budget.text = movie.budget.toString()
            revenue.text = movie.revenue.toString()
            year.text = movie.year.toString()
            description.text = movie.description
        }

        val imageUrl = TMDbImageHostProvider().getHostUrl() + movie.poster

        GlideApp.with(this)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.poster)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PARAM_MOVIE_ID = "MOVIE_ID"

        fun newInstance(movieId: Int) = DetailFragment().apply {
            arguments = Bundle().apply { putInt(PARAM_MOVIE_ID, movieId) }
        }
    }
}