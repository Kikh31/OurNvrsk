package com.nvrskapp.ournovorossiysk.data

import androidx.annotation.DrawableRes

data class News(
    val title: String,
    val description: String,
    val pubDate: String,
    @DrawableRes val resId: Int
)