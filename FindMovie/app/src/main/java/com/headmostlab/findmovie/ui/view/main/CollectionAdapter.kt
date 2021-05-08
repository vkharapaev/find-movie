package com.headmostlab.findmovie.ui.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.databinding.NarrowCollectionRowItemBinding
import com.headmostlab.findmovie.databinding.WideCollectionRowItemBinding
import com.headmostlab.findmovie.domain.entity.Collection
import com.headmostlab.findmovie.domain.entity.ShortMovie
import com.headmostlab.findmovie.ui.viewmodel.main.model.UiMovieCollection
import com.rubensousa.recyclerview.ScrollStateHolder

class CollectionAdapter(
    private val movieClickListener: (ShortMovie) -> Unit,
    private val showCollectionListener: (Collection) -> Unit,
    private val scrollHolder: ScrollStateHolder,
    private val lifecycleOwner: LifecycleOwner,
    private val pagerErrorHandler: (Throwable) -> Unit,
) :
    RecyclerView.Adapter<CollectionViewHolder>() {

    var movieCollection: List<UiMovieCollection> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            R.layout.narrow_collection_row_item -> NarrowCollectionRowItemBinding.inflate(inflater).root
            else -> WideCollectionRowItemBinding.inflate(inflater).root
        }
        return CollectionViewHolder(view, scrollHolder, movieClickListener, showCollectionListener, lifecycleOwner, pagerErrorHandler)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(showCollectionListener, movieCollection[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (movieCollection[position].showSecondLayout)
            R.layout.wide_collection_row_item else R.layout.narrow_collection_row_item
    }

    override fun onViewRecycled(holder: CollectionViewHolder) {
        super.onViewRecycled(holder)
        holder.onRecycled()
    }

    override fun getItemCount(): Int {
        return movieCollection.size
    }

    override fun onViewDetachedFromWindow(holder: CollectionViewHolder) {
        holder.fixSnapPosition()
    }
}
