package com.headmostlab.findmovie.ui.view.detail

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.AppBarLayout
import com.headmostlab.findmovie.App
import com.headmostlab.findmovie.DI
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.databinding.DetailFragmentBinding
import com.headmostlab.findmovie.domain.entity.FullMovie
import com.headmostlab.findmovie.ui.view.main.MainFragmentDirections
import com.headmostlab.findmovie.ui.view.nointernet.NoInternetFragment
import com.headmostlab.findmovie.ui.view.utils.VideoPlayer
import com.headmostlab.findmovie.ui.view.utils.showSnackbar
import com.headmostlab.findmovie.ui.view.utils.viewBinding
import com.headmostlab.findmovie.ui.viewmodel.detail.DetailViewModel
import com.headmostlab.findmovie.ui.viewmodel.detail.State
import java.io.IOException

class DetailFragment : Fragment(R.layout.detail_fragment) {

    private val arg: DetailFragmentArgs by navArgs()

    private val component by lazy {
        DaggerDetailScreenComponent.builder()
            .appContext(App.instance)
            .repository(DI.appComponent.repository())
            .build()
    }

    private val viewModel by viewModels<DetailViewModel> { component.viewModelFactory() }

    private val binding by viewBinding(DetailFragmentBinding::bind)

    private val videoPlayer by lazy { VideoPlayer(requireContext()) }

    private lateinit var actorAdapter: ActorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(NoInternetFragment.RETRY_TO_CONNECT) { requestKey, bundle -> }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getAppState(arg.movieId).observe(viewLifecycleOwner, { renderState(it) })

        viewModel.getVideoUrl(arg.movieId)
        viewModel.videoUrlLiveData.observe(viewLifecycleOwner) { url ->
            playVideo(url)
        }

        val windowHeight = requireActivity().window.decorView.height
        val videoPlayerHeight = windowHeight * 0.5f
        binding.appBarMaxHeight.layoutParams.height = videoPlayerHeight.toInt()
        binding.bottomContentHeaderHeight.layoutParams.height = videoPlayerHeight.toInt()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { _, inset ->
            val systemInsets = inset.getInsets(WindowInsetsCompat.Type.systemBars())

            (binding.headerLayout.header.layoutParams as FrameLayout.LayoutParams)
                .updateMargins(top = systemInsets.top)

            binding.main.updatePadding(bottom = systemInsets.bottom)

            binding.headerBottomMargin.layoutParams.height = systemInsets.bottom

            (binding.bottomMargin.layoutParams as ConstraintLayout.LayoutParams)
                .updateMargins(bottom = systemInsets.bottom)

            binding.bottomContent.minHeight = windowHeight - systemInsets.bottom

            inset
        }

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            videoPlayer.player.volume = 1 + verticalOffset.toFloat() / appBarLayout.totalScrollRange
        })

        actorAdapter = ActorAdapter()
        binding.actorsRecyclerView.adapter = actorAdapter
    }

    private fun renderState(state: State) {
        when (state) {
            State.Loading -> binding.apply {
                loadingProgress.shimmer.visibility = View.VISIBLE
                binding.content.visibility = View.INVISIBLE
            }
            is State.Success -> {
                binding.apply {
                    loadingProgress.shimmer.visibility = View.INVISIBLE
                    content.visibility = View.VISIBLE
                }
                setData(state.movie)
            }
            is State.Error -> {
                when (state.error) {
                    is IOException -> {
                        findNavController().navigate(MainFragmentDirections.actionGlobalNoInternetFragment())
                    }
                    else -> binding.apply {
                        loadingProgress.shimmer.visibility = View.INVISIBLE
                        main.showSnackbar(
                            state.error.message ?: getString(R.string.error_message),
                            getString(R.string.button_reload)
                        ) {
                            viewModel.getAppState(arg.movieId)
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        videoPlayer.stop()
    }

    override fun onResume() {
        super.onResume()

        viewModel.videoUrlLiveData.value?.let { url ->
            playVideo(url)
        }
    }

    private fun playVideo(url: String) {
        binding.playerView.player = videoPlayer.player
        videoPlayer.play(url)
    }

    private fun setData(movie: FullMovie) {
        val context = binding.root.context
        with(binding) {
            headerLayout.title.text = movie.title
            genres.text = movie.genres.joinToString()
            duration.text = context.getString(R.string.detail_duration, movie.duration.toString())
            rating.text = context.getString(
                R.string.detail_rating,
                movie.votesAverage.toString()
            )
            date.text = if (movie.date.length > 4) movie.date.subSequence(0..3) else movie.date
            description.text = movie.description
        }
        actorAdapter.submitList(movie.people)
    }

}