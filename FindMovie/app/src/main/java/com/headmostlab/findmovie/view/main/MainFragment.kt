package com.headmostlab.findmovie.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.databinding.MainFragmentBinding
import com.headmostlab.findmovie.viewmodel.main.AppState
import com.headmostlab.findmovie.viewmodel.main.MainViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MovieAdapter
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = MovieAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(createDividerDecoration())
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getAppStateLiveData().observe(viewLifecycleOwner, { renderAppState(it) })
        viewModel.getMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderAppState(state: AppState): Unit = when (state) {
        AppState.Loading -> {
            binding.loadingProgress.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.INVISIBLE
        }
        is AppState.Success -> {
            adapter.submitList(state.movies)
            binding.loadingProgress.visibility = View.INVISIBLE
            binding.recyclerView.visibility = View.VISIBLE
        }
        is AppState.Error -> {
            binding.loadingProgress.visibility = View.INVISIBLE
            Snackbar
                .make(binding.main, getString(R.string.error_message), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.button_reload)) { viewModel.getMovies() }
                .show()
        }
    }

    private fun createDividerDecoration(): DividerItemDecoration {
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.divider, null)
        drawable?.apply { decoration.setDrawable(drawable) }
        return decoration
    }

}