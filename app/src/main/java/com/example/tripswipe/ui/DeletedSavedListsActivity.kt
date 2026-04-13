package com.example.tripswipe.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripswipe.adapter.SavedListAdapter
import com.example.tripswipe.databinding.ActivityDeletedSavedListsBinding
import com.example.tripswipe.storage.SavedListStorage
import com.google.android.material.snackbar.Snackbar

class DeletedSavedListsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeletedSavedListsBinding
    private lateinit var adapter: SavedListAdapter
    private lateinit var storage: SavedListStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeletedSavedListsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = SavedListStorage(this)

        adapter = SavedListAdapter(
            items = emptyList(),
            deletedMode = true,
            onClick = {},
            onActionClick = { savedList ->
                storage.restoreList(savedList.id)
                loadDeletedLists()
                Snackbar.make(binding.root, "Restored ${savedList.name} to Saved Lists.", Snackbar.LENGTH_SHORT).show()
            }
        )

        binding.recyclerDeletedSavedLists.layoutManager = LinearLayoutManager(this)
        binding.recyclerDeletedSavedLists.adapter = adapter

        binding.buttonBackToSavedLists.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadDeletedLists()
    }

    private fun loadDeletedLists() {
        val deletedLists = storage.getDeletedLists()
        adapter.submitList(deletedLists)
        binding.textEmpty.visibility = if (deletedLists.isEmpty()) View.VISIBLE else View.GONE
        binding.recyclerDeletedSavedLists.visibility = if (deletedLists.isEmpty()) View.GONE else View.VISIBLE
    }
}
