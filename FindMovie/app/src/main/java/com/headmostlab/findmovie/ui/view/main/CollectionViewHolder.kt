package com.headmostlab.findmovie.ui.view.main

import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.domain.entity.Collection
import com.headmostlab.findmovie.domain.entity.ShortMovie
import com.headmostlab.findmovie.ui.viewmodel.main.model.UiMovieCollection
import com.rubensousa.recyclerview.ScrollStateHolder

class CollectionViewHolder(
    private val view: View,
    private val scrollState: ScrollStateHolder,
    private val listener: (ShortMovie) -> Unit,
    private val showCollectionListener: (Collection) -> Unit,
    private val lifecycleOwner: LifecycleOwner,
    private val pagerErrorHandler: (Throwable) -> Unit
) :
    RecyclerView.ViewHolder(view), ScrollStateHolder.ScrollStateKeyProvider {

    private var stateKey: String? = null
    private var data: UiMovieCollection? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var shimmer: ShimmerFrameLayout
    private lateinit var title: TextView
    private var layoutManager: RecyclerView.LayoutManager? = null
    private val snapHelper: GravitySnapHelper by lazy {
        GravitySnapHelper(Gravity.START).apply {
            scrollMsPerInch = 50f
            snapToPadding = true
        }
    }

    private fun bindViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        title = view.findViewById(R.id.title)
        shimmer = view.findViewById(R.id.shimmerLayout)
    }

    fun bind(movieClickListener: (Collection) -> Unit, item: UiMovieCollection) {
        bindViews(view)

        data = item
        stateKey = item.collection.id.toString()

        scrollState.setupRecyclerView(recyclerView, this)

        layoutManager = recyclerView.layoutManager

        title.text = title.context.getText(item.collection.eCollection.title)
        title.setOnClickListener {
            movieClickListener.invoke(item.collection)
        }

        val adapter = MovieAdapter(
            listener, showCollectionListener,
            item.showSecondLayout
        )

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                recyclerView.visibility = View.VISIBLE
                shimmer.visibility = View.GONE
            }
        })

        adapter.addLoadStateListener {

            if (it.refresh is LoadState.Loading) {
                recyclerView.visibility = View.GONE
                shimmer.visibility = View.VISIBLE
                shimmer.startShimmer()
            }

            if (it.append is LoadState.Error) {
                pagerErrorHandler((it.append as LoadState.Error).error)
            }
        }

        item.movies.observe(lifecycleOwner) {
            adapter.submitData(lifecycleOwner.lifecycle, it)
        }

        recyclerView.adapter = adapter
        snapHelper.attachToRecyclerView(recyclerView)
        scrollState.restoreScrollState(recyclerView, this)
    }

    fun onRecycled() {
        data?.movies?.removeObservers(lifecycleOwner)
        scrollState.saveScrollState(recyclerView, this)
    }

    override fun getScrollStateKey(): String? {
        return stateKey
    }

    fun fixSnapPosition() {
        layoutManager?.let { lm ->
            val snap = snapHelper
            snap.findSnapView(lm)?.let {
                val out = snap.calculateDistanceToFinalSnap(lm, it)
                recyclerView.scrollBy(out[0], out[1])
            }
        }
    }
}
