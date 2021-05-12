package com.headmostlab.findmovie.ui.view.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import com.headmostlab.findmovie.R

class PlayerBehaviour @JvmOverloads constructor(
    context: Context, attrs: AttributeSet
) : CoordinatorLayout.Behavior<View>(context, attrs) {

    private var startHeight: Float = 0f
    private var finalHeight: Float = 0f

    init {
        lateinit var a: TypedArray
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.PlayerBehavior)
            startHeight = a.getDimension(R.styleable.PlayerBehavior_startHeight, startHeight)
            finalHeight = a.getDimension(R.styleable.PlayerBehavior_finalHeight, finalHeight)
        } finally {
            a.recycle()
        }
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is NestedScrollView
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val depY = dependency.y

        val percent = depY / (parent.height * 0.5f) // percent to parent center

        val diffHeight = startHeight - finalHeight

        val newHeight = Math.max(startHeight - diffHeight * percent * 2, finalHeight).toInt()
        child.layoutParams.height = newHeight
        var newY = Math.max(
            Math.min(
                (depY + finalHeight - newHeight),
                (parent.height - newHeight) * 0.5f // if child center grater then parent center
            ), 0f
        )
        newY -= newHeight * 0.08f  * (1 - percent) // move player a bit off screen
        child.y = newY
        child.requestLayout()
        return false
    }
}