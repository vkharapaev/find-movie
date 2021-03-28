package com.headmostlab.findmovie.view.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.databinding.DetailFragmentBinding

class DetailFragment: Fragment(R.layout.detail_fragment) {

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!
    private var movieId: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = DetailFragmentBinding.bind(view)
        movieId = arguments?.getInt(PARAM_MOVIE_ID)
            ?: throw RuntimeException(getString(R.string.detail_fragment_movie_id_is_null))
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