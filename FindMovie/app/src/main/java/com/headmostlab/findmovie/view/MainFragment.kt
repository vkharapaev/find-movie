package com.headmostlab.findmovie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.viewmodel.AppState
import com.headmostlab.findmovie.viewmodel.MainViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getAppStateLiveData().observe(viewLifecycleOwner, { renderAppState(it) })
        viewModel.getMovies()
    }

    private fun renderAppState(state: AppState): Unit = when (state) {
        AppState.Loading -> TODO()
        is AppState.Success -> TODO()
    }

}