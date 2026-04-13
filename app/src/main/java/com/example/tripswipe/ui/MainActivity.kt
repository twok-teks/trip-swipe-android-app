package com.example.tripswipe.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripswipe.adapter.CityAdapter
import com.example.tripswipe.adapter.SavedListAdapter
import com.example.tripswipe.auth.UserSessionStorage
import com.example.tripswipe.data.TripRepository
import com.example.tripswipe.databinding.ActivityMainBinding
import com.example.tripswipe.storage.SavedListStorage
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var savedListAdapter: SavedListAdapter
    private lateinit var cityAdapter: CityAdapter
    private lateinit var sessionStorage: UserSessionStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionStorage = UserSessionStorage(this)
        if (!sessionStorage.isLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }

        binding.recyclerCities.layoutManager = LinearLayoutManager(this)
        cityAdapter = CityAdapter(TripRepository.cachedCities()) { city ->
            val intent = Intent(this, SwipeActivity::class.java)
            intent.putExtra(SwipeActivity.EXTRA_CITY, city.name)
            startActivity(intent)
        }
        binding.recyclerCities.adapter = cityAdapter

        savedListAdapter = SavedListAdapter(
            items = emptyList(),
            deletedMode = false,
            onClick = { savedList ->
                val intent = Intent(this, SavedListDetailActivity::class.java)
                intent.putExtra(SavedListDetailActivity.EXTRA_LIST_ID, savedList.id)
                startActivity(intent)
            },
            onActionClick = { savedList ->
                SavedListStorage(this).deleteList(savedList.id)
                refreshSavedLists()
                Snackbar.make(binding.root, "Moved ${savedList.name} to deleted saved lists.", Snackbar.LENGTH_SHORT).show()
            }
        )
        binding.recyclerSavedLists.layoutManager = LinearLayoutManager(this)
        binding.recyclerSavedLists.adapter = savedListAdapter

        binding.buttonTabCities.setOnClickListener { showCitiesTab() }
        binding.buttonTabSaved.setOnClickListener { showSavedTab() }
        binding.buttonDeletedSavedLists.setOnClickListener {
            startActivity(Intent(this, DeletedSavedListsActivity::class.java))
        }
        binding.buttonProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        showCitiesTab()

        lifecycleScope.launch {
            cityAdapter.submitList(TripRepository.cities())
        }
    }

    override fun onResume() {
        super.onResume()
        refreshSavedLists()
        if (binding.recyclerSavedLists.visibility == View.VISIBLE) {
            updateSavedTabText()
        }
    }

    private fun refreshSavedLists() {
        val savedLists = SavedListStorage(this).getAllLists()
        savedListAdapter.submitList(savedLists)
        binding.textSavedEmpty.visibility = if (savedLists.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showCitiesTab() {
        binding.buttonTabCities.isChecked = true
        binding.buttonTabSaved.isChecked = false
        binding.recyclerCities.visibility = View.VISIBLE
        binding.recyclerSavedLists.visibility = View.GONE
        binding.textSavedEmpty.visibility = View.GONE
        binding.buttonDeletedSavedLists.visibility = View.GONE
    }

    private fun showSavedTab() {
        binding.buttonTabCities.isChecked = false
        binding.buttonTabSaved.isChecked = true
        binding.recyclerCities.visibility = View.GONE
        binding.recyclerSavedLists.visibility = View.VISIBLE
        binding.buttonDeletedSavedLists.visibility = View.VISIBLE
        refreshSavedLists()
        updateSavedTabText()
    }

    private fun updateSavedTabText() {
        val savedLists = SavedListStorage(this).getAllLists()
        binding.textSavedEmpty.visibility = if (savedLists.isEmpty()) View.VISIBLE else View.GONE
    }
}
