package com.example.tripswipe.storage

import android.content.Context
import com.example.tripswipe.auth.UserSessionStorage
import com.example.tripswipe.data.TripRepository
import com.example.tripswipe.model.SavedItinerary
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SavedListStorage(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val currentUserKey = UserSessionStorage(context).getCurrentUserKey()

    fun saveList(name: String, city: String, attractionIds: List<Int>) {
        if (name.isBlank() || attractionIds.isEmpty()) return

        val all = getRawArray()
        val item = JSONObject().apply {
            put("id", System.currentTimeMillis())
            put("name", name.trim())
            put("city", city)
            put("createdAtLabel", timestampLabel())
            put("attractionIds", JSONArray(attractionIds))
            put("deletedAttractionIds", JSONArray())
            put("isDeleted", false)
            put("deletedAtLabel", JSONObject.NULL)
        }
        all.put(item)
        saveRawArray(all)
    }

    fun getAllLists(): List<SavedItinerary> = getLists(includeDeleted = false)

    fun getDeletedLists(): List<SavedItinerary> = getLists(includeDeleted = true).filter { it.isDeleted }

    private fun getLists(includeDeleted: Boolean): List<SavedItinerary> {
        val output = mutableListOf<SavedItinerary>()
        val raw = getRawArray()
        for (i in 0 until raw.length()) {
            val obj = raw.optJSONObject(i) ?: continue
            val isDeleted = obj.optBoolean("isDeleted", false)
            if (!includeDeleted && isDeleted) continue

            val activeIds = jsonArrayToIntList(obj.optJSONArray("attractionIds"))
            val deletedIds = jsonArrayToIntList(obj.optJSONArray("deletedAttractionIds"))
            output += SavedItinerary(
                id = obj.optLong("id"),
                name = obj.optString("name"),
                city = obj.optString("city"),
                attractions = activeIds.mapNotNull { TripRepository.attractionById(it) },
                deletedAttractions = deletedIds.mapNotNull { TripRepository.attractionById(it) },
                createdAtLabel = obj.optString("createdAtLabel"),
                isDeleted = isDeleted,
                deletedAtLabel = obj.optString("deletedAtLabel").takeIf { it.isNotBlank() && it != "null" }
            )
        }
        return output.sortedByDescending { it.id }
    }

    fun getListById(id: Long): SavedItinerary? = getLists(includeDeleted = true).firstOrNull { it.id == id && !it.isDeleted }

    fun moveAttractionToDeleted(listId: Long, attractionId: Int) {
        val raw = getRawArray()
        for (i in 0 until raw.length()) {
            val obj = raw.optJSONObject(i) ?: continue
            if (obj.optLong("id") == listId) {
                val active = jsonArrayToMutableIntList(obj.optJSONArray("attractionIds"))
                val deleted = jsonArrayToMutableIntList(obj.optJSONArray("deletedAttractionIds"))
                if (active.remove(attractionId) && !deleted.contains(attractionId)) {
                    deleted.add(attractionId)
                }
                obj.put("attractionIds", JSONArray(active))
                obj.put("deletedAttractionIds", JSONArray(deleted))
                break
            }
        }
        saveRawArray(raw)
    }

    fun restoreAttraction(listId: Long, attractionId: Int) {
        val raw = getRawArray()
        for (i in 0 until raw.length()) {
            val obj = raw.optJSONObject(i) ?: continue
            if (obj.optLong("id") == listId) {
                val active = jsonArrayToMutableIntList(obj.optJSONArray("attractionIds"))
                val deleted = jsonArrayToMutableIntList(obj.optJSONArray("deletedAttractionIds"))
                if (deleted.remove(attractionId) && !active.contains(attractionId)) {
                    active.add(attractionId)
                }
                obj.put("attractionIds", JSONArray(active))
                obj.put("deletedAttractionIds", JSONArray(deleted))
                break
            }
        }
        saveRawArray(raw)
    }

    fun deleteList(listId: Long) {
        val raw = getRawArray()
        for (i in 0 until raw.length()) {
            val obj = raw.optJSONObject(i) ?: continue
            if (obj.optLong("id") == listId) {
                obj.put("isDeleted", true)
                obj.put("deletedAtLabel", timestampLabel())
                break
            }
        }
        saveRawArray(raw)
    }

    fun restoreList(listId: Long) {
        val raw = getRawArray()
        for (i in 0 until raw.length()) {
            val obj = raw.optJSONObject(i) ?: continue
            if (obj.optLong("id") == listId) {
                obj.put("isDeleted", false)
                obj.put("deletedAtLabel", JSONObject.NULL)
                break
            }
        }
        saveRawArray(raw)
    }

    private fun jsonArrayToIntList(array: JSONArray?): List<Int> {
        val ids = mutableListOf<Int>()
        if (array == null) return ids
        for (j in 0 until array.length()) ids += array.optInt(j)
        return ids
    }

    private fun jsonArrayToMutableIntList(array: JSONArray?): MutableList<Int> = jsonArrayToIntList(array).toMutableList()

    private fun getRawArray(): JSONArray {
        val saved = prefs.getString(listsKey(), null)
        return if (saved.isNullOrBlank()) JSONArray() else JSONArray(saved)
    }

    private fun saveRawArray(array: JSONArray) {
        prefs.edit().putString(listsKey(), array.toString()).apply()
    }

    private fun timestampLabel(): String {
        val formatter = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.US)
        return formatter.format(Date())
    }

    private fun listsKey(): String = "saved_lists_$currentUserKey"

    companion object {
        private const val PREFS_NAME = "trip_swipe_saved_lists"
    }
}
