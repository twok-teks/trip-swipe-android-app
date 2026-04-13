package com.example.tripswipe.model

import java.io.Serializable

data class SavedItinerary(
    val id: Long,
    val name: String,
    val city: String,
    val attractions: List<Attraction>,
    val deletedAttractions: List<Attraction> = emptyList(),
    val createdAtLabel: String,
    val isDeleted: Boolean = false,
    val deletedAtLabel: String? = null
) : Serializable
