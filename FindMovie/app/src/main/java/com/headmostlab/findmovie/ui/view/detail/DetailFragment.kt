package com.headmostlab.findmovie.ui.view.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.headmostlab.findmovie.App
import com.headmostlab.findmovie.GlideApp
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.databinding.DetailFragmentBinding
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.data.repository.RepositoryImpl
import com.headmostlab.findmovie.data.datasource.network.TMDbDataSource
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApi
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbApiKeyProvider
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbHostProvider
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbImageHostProvider
import com.headmostlab.findmovie.data.repository.MockRepository
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
        val dataSource = TMDbDataSource(service, TMDbApiKeyProvider())
        val repository = /*MockRepository()*/ RepositoryImpl(dataSource, App.instance.database)
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
                    main.showSnackbar(
                        state.error.message ?: getString(R.string.error_message),
                        getString(R.string.button_reload)
                    ) {
                        viewModel.getAppState(storedMovieId)
                    }
                }
            }
        }
    }

    private fun setData(movie: FullMovie) {
        val context = binding.root.context
        with(binding) {
            title.text = movie.title
            origTitle.text = movie.origTitle
            genres.text = genresToString(movie.genres)
            duration.text = context.getString(R.string.detail_duration, movie.duration.toString())
            rating.text = "${movie.votesAverage} (${movie.votesCount})"
            budget.text = context.getString(R.string.detail_budget, movie.budget.toString())
            revenue.text = context.getString(R.string.detail_revenue, movie.revenue.toString())
            date.text = context.getString(R.string.detail_release_date, movie.date)
            description.text = movie.description
            movieId.text = movie.id.toString()
        }

        val imageUrl = TMDbImageHostProvider().getHostUrl() + movie.poster

        GlideApp.with(this)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.poster)
    }

    private fun genresToString(genres: List<String>) =
        genres.toString().removePrefix("[").removeSuffix("]")

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