package com.headmostlab.findmovie.ui.view.utils

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    text: String,
    actionText: String,
    action: (View) -> Unit
) {
    Snackbar.make(this, text, Snackbar.LENGTH_INDEFINITE).setAction(actionText, action).show()
}

fun View.showSnackbar(@StringRes text: Int) {
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}

fun View.showSnackbar(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}