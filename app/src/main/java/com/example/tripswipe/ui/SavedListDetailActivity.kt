package com.example.tripswipe.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripswipe.adapter.ResultsAdapter
import com.example.tripswipe.databinding.ActivitySavedListDetailBinding
import com.example.tripswipe.model.Attraction
import com.example.tripswipe.model.SavedItinerary
import com.example.tripswipe.storage.SavedListStorage
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar

class SavedListDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedListDetailBinding
    private lateinit var adapter: ResultsAdapter
    private lateinit var storage: SavedListStorage
    private var savedList: SavedItinerary? = null
    private var attractions: MutableList<Attraction> = mutableListOf()
    private var deletedAttractions: MutableList<Attraction> = mutableListOf()
    private var showingDeleted = false
    private var selectedCategory = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = SavedListStorage(this)

        val listId = intent.getLongExtra(EXTRA_LIST_ID, -1L)
        savedList = storage.getListById(listId)

        if (savedList == null) {
            binding.textTitle.text = "Saved list"
            binding.textSubtitle.text = "This list could not be loaded."
            binding.textEmpty.visibility = View.VISIBLE
            binding.recyclerResults.visibility = View.GONE
            binding.buttonBackHome.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            }
            return
        }

        attractions = savedList!!.attractions.toMutableList()
        deletedAttractions = savedList!!.deletedAttractions.toMutableList()
        binding.textTitle.text = savedList!!.name
        binding.textSubtitle.text = "${savedList!!.city} • ${savedList!!.createdAtLabel}"

        adapter = ResultsAdapter(emptyList()) { attraction ->
            if (showingDeleted) restoreAttraction(attraction) else deleteAttraction(attraction)
        }
        binding.recyclerResults.layoutManager = LinearLayoutManager(this)
        binding.recyclerResults.adapter = adapter

        binding.buttonTabList.setOnClickListener {
            showingDeleted = false
            renderCurrentState()
        }
        binding.buttonTabDeleted.setOnClickListener {
            showingDeleted = true
            renderCurrentState()
        }
        binding.buttonBackHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        setupChips()
        renderCurrentState()
    }

    private fun setupChips() {
        val allItems = (attractions + deletedAttractions).distinctBy { it.id }
        val categories = listOf("All") + allItems.map { it.category }.distinct().sorted()
        binding.chipGroup.removeAllViews()
        categories.forEachIndexed { index, category ->
            val chip = Chip(this).apply {
                text = category
                isCheckable = true
                isClickable = true
                isChecked = index == 0
                setOnClickListener {
                    selectedCategory = category
                    renderCurrentState()
                }
            }
            binding.chipGroup.addView(chip)
        }
    }

    private fun renderCurrentState() {
        binding.buttonTabList.isChecked = !showingDeleted
        binding.buttonTabDeleted.isChecked = showingDeleted
        val source = if (showingDeleted) deletedAttractions else attractions
        val filtered = if (selectedCategory == "All") source else source.filter { it.category == selectedCategory }
        adapter.submitList(filtered, showingDeleted)
        updateEmptyState(filtered)
        binding.textEmpty.text = if (showingDeleted) {
            "No deleted attractions in this saved list."
        } else {
            "No attractions match this filter in this saved list."
        }
    }

    private fun deleteAttraction(attraction: Attraction) {
        val listId = savedList?.id ?: return
        if (attractions.removeAll { it.id == attraction.id }) {
            if (deletedAttractions.none { it.id == attraction.id }) deletedAttractions.add(attraction)
            storage.moveAttractionToDeleted(listId, attraction.id)
            Snackbar.make(binding.root, "Moved ${attraction.name} to Deleted.", Snackbar.LENGTH_SHORT).show()
            renderCurrentState()
        }
    }

    private fun restoreAttraction(attraction: Attraction) {
        val listId = savedList?.id ?: return
        if (deletedAttractions.removeAll { it.id == attraction.id }) {
            if (attractions.none { it.id == attraction.id }) attractions.add(attraction)
            storage.restoreAttraction(listId, attraction.id)
            Snackbar.make(binding.root, "Restored ${attraction.name} to List.", Snackbar.LENGTH_SHORT).show()
            renderCurrentState()
        }
    }

    private fun updateEmptyState(current: List<Attraction>) {
        binding.textEmpty.visibility = if (current.isEmpty()) View.VISIBLE else View.GONE
        binding.recyclerResults.visibility = if (current.isEmpty()) View.GONE else View.VISIBLE
    }

    companion object {
        const val EXTRA_LIST_ID = "extra_list_id"
    }
}
