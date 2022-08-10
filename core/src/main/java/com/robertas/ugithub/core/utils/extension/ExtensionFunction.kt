package com.robertas.ugithub.core.utils.extension

import android.view.View
import android.widget.TextView

fun TextView.setTextOrHide(newText: String) {
    if (newText.isEmpty() || newText.isBlank()) {

        this.visibility = View.GONE

    } else {

        this.apply {
            visibility = View.VISIBLE

            text = newText
        }
    }
}