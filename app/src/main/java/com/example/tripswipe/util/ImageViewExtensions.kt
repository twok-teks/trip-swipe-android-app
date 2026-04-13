package com.example.tripswipe.util

import android.widget.ImageView
import coil.load
import com.example.tripswipe.model.GalleryImage

fun ImageView.loadGalleryImage(image: GalleryImage) {
    val remoteUrl = image.remoteUrl?.takeIf { it.isNotBlank() }
    if (remoteUrl != null) {
        load(remoteUrl) {
            crossfade(true)
            image.localResId?.let { fallbackRes ->
                placeholder(fallbackRes)
                error(fallbackRes)
            }
        }
    } else {
        setImageResource(image.localResId ?: 0)
    }
}
