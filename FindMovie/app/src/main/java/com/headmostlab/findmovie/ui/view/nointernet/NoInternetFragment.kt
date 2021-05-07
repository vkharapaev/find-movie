package com.headmostlab.findmovie.ui.view.nointernet

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.databinding.NoInternetFragmentBinding
import com.headmostlab.findmovie.ui.view.main.MainFragment
import com.headmostlab.findmovie.ui.view.utils.viewBinding

class NoInternetFragment : Fragment(R.layout.no_internet_fragment) {
    companion object {
        fun newInstance() = NoInternetFragment()
    }

    private val binding by viewBinding(NoInternetFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.retryButton.setOnClickListener {
            parentFragmentManager.setFragmentResult("RETRY_TO_CONNECT", bundleOf())
            parentFragmentManager.popBackStack()
        }
    }
}