package com.headmostlab.findmovie.ui.view.utils

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.headmostlab.findmovie.R

fun RecyclerView.addDivider(orientation: Int = DividerItemDecoration.VERTICAL) {
    val drawable = ResourcesCompat.getDrawable(resources, R.drawable.divider, null)
    val divider = DividerItemDecoration(this.context, orientation)
    drawable?.let { divider.setDrawable(it) }
    addItemDecoration(divider)
}
