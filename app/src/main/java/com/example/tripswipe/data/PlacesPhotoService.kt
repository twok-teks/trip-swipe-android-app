package com.example.tripswipe.data

import com.example.tripswipe.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class PlacesPhotoService(
    private val client: OkHttpClient = OkHttpClient()
) {
    suspend fun photoUrlsForQuery(query: String, limit: Int = 3): List<String> = withContext(Dispatchers.IO) {
        if (!AppConfig.isPlacesConfigured || query.isBlank()) return@withContext emptyList()

        val body = JSONObject()
            .put("textQuery", query)
            .put("maxResultCount", 1)
            .put("languageCode", "en")
            .toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(TEXT_SEARCH_URL)
            .post(body)
            .header("Content-Type", "application/json")
            .header("X-Goog-Api-Key", AppConfig.placesApiKey)
            .header("X-Goog-FieldMask", "places.photos")
            .build()

        runCatching {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@use emptyList()

                val responseBody = response.body?.string().orEmpty()
                val photos = JSONObject(responseBody)
                    .optJSONArray("places")
                    ?.optJSONObject(0)
                    ?.optJSONArray("photos")
                    ?: return@use emptyList()

                buildList {
                    for (index in 0 until minOf(limit, photos.length())) {
                        val photoName = photos.optJSONObject(index)?.optString("name").orEmpty()
                        if (photoName.isNotBlank()) {
                            add(buildPhotoUrl(photoName))
                        }
                    }
                }
            }
        }.getOrDefault(emptyList())
    }

    suspend fun photoUrlsForQueries(queries: List<String>, limitPerQuery: Int = 3): Map<String, List<String>> = coroutineScope {
        queries
            .distinct()
            .associateWith { query ->
                async { photoUrlsForQuery(query, limitPerQuery) }
            }
            .mapValues { it.value.await() }
    }

    private fun buildPhotoUrl(photoName: String): String {
        val encodedPath = photoName
            .split("/")
            .joinToString("/") { URLEncoder.encode(it, StandardCharsets.UTF_8.toString()) }
        return "$PLACES_BASE_URL/$encodedPath/media?maxHeightPx=1200&key=${AppConfig.placesApiKey}"
    }

    companion object {
        private const val PLACES_BASE_URL = "https://places.googleapis.com/v1"
        private const val TEXT_SEARCH_URL = "$PLACES_BASE_URL/places:searchText"
    }
}
