package com.example.tripswipe.model

import androidx.annotation.DrawableRes

data class City(
    val name: String,
    val subtitle: String,
    val imageUrl: String? = null,
    @DrawableRes val fallbackImageRes: Int,
    val imageSearchQuery: String = "$name skyline cityscape"
)
