package com.example.tripswipe.model

import androidx.annotation.DrawableRes
import java.io.Serializable

data class Attraction(
    val id: Int,
    val city: String,
    val name: String,
    val category: String,
    val description: String,
    val imageUrls: List<String> = emptyList(),
    @DrawableRes val fallbackImageResIds: List<Int>,
    val websiteUrl: String,
    val mapUrl: String,
    val imageSearchQuery: String = "$name $city"
) : Serializable {
    val galleryItems: List<GalleryImage>
        get() = if (imageUrls.isNotEmpty()) {
            imageUrls.map { GalleryImage(remoteUrl = it) }
        } else {
            fallbackImageResIds.map { GalleryImage(localResId = it) }
        }

    val imageCount: Int
        get() = galleryItems.size
}
