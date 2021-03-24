package com.nvrskapp.ournovorossiysk.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("srcCompat")
fun ImageView.bindSrcRes(resId: Int) {
    this.setImageResource(resId)
}