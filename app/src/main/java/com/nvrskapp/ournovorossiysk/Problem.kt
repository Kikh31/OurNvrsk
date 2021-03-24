package com.nvrskapp.ournovorossiysk

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Problem(
    var title: String? = "",
    var address: String? = "",
    var image: String? = "",
    var description: String? = ""
)