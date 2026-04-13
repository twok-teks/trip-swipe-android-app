package com.example.tripswipe.model

import androidx.annotation.DrawableRes
import java.io.Serializable

data class GalleryImage(
    val remoteUrl: String? = null,
    @DrawableRes val localResId: Int? = null
) : Serializable
