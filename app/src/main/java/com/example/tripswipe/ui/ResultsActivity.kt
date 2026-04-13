package com.example.tripswipe.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripswipe.adapter.ResultsAdapter
import com.example.tripswipe.databinding.ActivityResultsBinding
import com.example.tripswipe.model.Attraction
import com.example.tripswipe.storage.SavedListStorage
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar

class ResultsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultsBinding
    private lateinit var adapter: ResultsAdapter
    private lateinit var liked: ArrayList<Attraction>
    private lateinit var city: String
    private val deleted = arrayListOf<Attraction>()
    private var showingDeleted = false
    private var selectedCategory = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        city = intent.getStringExtra(EXTRA_CITY).orEmpty()
        liked = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_LIKED, ArrayList::class.java) as? ArrayList<Attraction>
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(EXTRA_LIKED) as? ArrayList<Attraction>
        } ?: arrayListOf()

        binding.textTitle.text = "$city Picks"

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

        binding.buttonHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
        binding.buttonSaveList.setOnClickListener { showSaveDialog() }

        setupChips()
        renderCurrentState()
    }

    private fun setupChips() {
        val allItems = (liked + deleted).distinctBy { it.id }
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
        val source = if (showingDeleted) deleted else liked
        val filtered = if (selectedCategory == "All") source else source.filter { it.category == selectedCategory }
        adapter.submitList(filtered, showingDeleted)
        updateEmptyState(filtered)
        binding.buttonSaveList.isEnabled = liked.isNotEmpty()
        binding.textEmpty.text = if (showingDeleted) {
            "No deleted attractions here yet."
        } else {
            "No attractions match this filter in your current list."
        }
    }

    private fun updateEmptyState(current: List<Attraction>) {
        binding.textEmpty.visibility = if (current.isEmpty()) View.VISIBLE else View.GONE
        binding.recyclerResults.visibility = if (current.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun deleteAttraction(attraction: Attraction) {
        if (liked.removeAll { it.id == attraction.id }) {
            if (deleted.none { it.id == attraction.id }) deleted.add(attraction)
            Snackbar.make(binding.root, "Moved ${attraction.name} to Deleted.", Snackbar.LENGTH_SHORT).show()
            renderCurrentState()
        }
    }

    private fun restoreAttraction(attraction: Attraction) {
        if (deleted.removeAll { it.id == attraction.id }) {
            if (liked.none { it.id == attraction.id }) liked.add(attraction)
            Snackbar.make(binding.root, "Restored ${attraction.name} to List.", Snackbar.LENGTH_SHORT).show()
            renderCurrentState()
        }
    }

    private fun showSaveDialog() {
        if (liked.isEmpty()) {
            Snackbar.make(binding.root, "Save at least one attraction in the List tab first.", Snackbar.LENGTH_SHORT).show()
            return
        }

        val input = EditText(this).apply {
            hint = "My dreamy city list"
            setText("$city Favorites")
            setPadding(48, 36, 48, 36)
        }

        AlertDialog.Builder(this)
            .setTitle("Save this list")
            .setMessage("Give your swipe results a name so they show up in the Saved Lists tab.")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isBlank()) {
                    Snackbar.make(binding.root, "Please enter a list name.", Snackbar.LENGTH_SHORT).show()
                } else {
                    SavedListStorage(this).saveList(name, city, liked.map { it.id })
                    Snackbar.make(binding.root, "Saved to your Saved Lists tab.", Snackbar.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    companion object {
        const val EXTRA_CITY = "extra_city"
        const val EXTRA_LIKED = "extra_liked"
    }
}
