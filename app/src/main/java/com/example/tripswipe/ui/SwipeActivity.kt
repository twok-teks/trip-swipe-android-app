package com.example.tripswipe.ui

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripswipe.adapter.SwipeAdapter
import com.example.tripswipe.data.TripRepository
import com.example.tripswipe.databinding.ActivitySwipeBinding
import com.example.tripswipe.model.Attraction
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class SwipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySwipeBinding
    private lateinit var adapter: SwipeAdapter
    private val liked = arrayListOf<Attraction>()
    private lateinit var city: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        city = intent.getStringExtra(EXTRA_CITY).orEmpty()
        binding.textTitle.text = "$city Swipe Deck"

        adapter = SwipeAdapter(
            items = mutableListOf(),
            onAttractionClick = ::openAttractionDetail
        )
        binding.recyclerSwipe.layoutManager = LinearLayoutManager(this)
        binding.recyclerSwipe.adapter = adapter

        val touchHelper = ItemTouchHelper(swipeCallback())
        touchHelper.attachToRecyclerView(binding.recyclerSwipe)

        binding.buttonLike.setOnClickListener { swipeTop(ItemTouchHelper.RIGHT) }
        binding.buttonSkip.setOnClickListener { swipeTop(ItemTouchHelper.LEFT) }
        binding.buttonFinish.setOnClickListener { openResults() }
        binding.buttonCancel.setOnClickListener { confirmCancel() }

        onBackPressedDispatcher.addCallback(this) {
            confirmCancel()
        }

        lifecycleScope.launch {
            val attractions = TripRepository.attractionsForCity(city)
            adapter.submitItems(attractions)
            updateCount()
        }
    }

    private fun swipeTop(direction: Int) {
        val holder = binding.recyclerSwipe.findViewHolderForAdapterPosition(0) ?: return
        val dx = if (direction == ItemTouchHelper.RIGHT) binding.recyclerSwipe.width.toFloat() else -binding.recyclerSwipe.width.toFloat()
        holder.itemView.animate()
            .translationX(dx)
            .alpha(0f)
            .setDuration(180)
            .withEndAction {
                holder.itemView.translationX = 0f
                holder.itemView.alpha = 1f
                handleSwipe(0, direction)
            }
            .start()
    }

    private fun swipeCallback(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                handleSwipe(viewHolder.bindingAdapterPosition, direction)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                viewHolder.itemView.alpha = (1f - kotlin.math.min(1f, kotlin.math.abs(dX) / recyclerView.width)) * 0.5f + 0.5f
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
    }

    private fun handleSwipe(position: Int, direction: Int) {
        if (position == RecyclerView.NO_POSITION || adapter.itemCount == 0) return
        val attraction = adapter.removeAt(position)
        if (direction == ItemTouchHelper.RIGHT) liked.add(attraction)
        adapter.notifyItemRemoved(position)
        updateCount()
        if (adapter.itemCount == 0) {
            openResults()
        } else if (direction == ItemTouchHelper.RIGHT) {
            Snackbar.make(binding.root, "Saved ${attraction.name}", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun updateCount() {
        binding.textCounter.text = "Saved ${liked.size} of ${TripRepository.cachedAttractionsForCity(city).size}"
    }

    private fun openResults() {
        val intent = Intent(this, ResultsActivity::class.java)
        intent.putExtra(ResultsActivity.EXTRA_CITY, city)
        intent.putExtra(ResultsActivity.EXTRA_LIKED, liked)
        startActivity(intent)
    }

    private fun openAttractionDetail(attraction: Attraction) {
        val intent = Intent(this, AttractionDetailActivity::class.java)
        intent.putExtra(AttractionDetailActivity.EXTRA_ATTRACTION, attraction)
        startActivity(intent)
    }

    private fun confirmCancel() {
        AlertDialog.Builder(this)
            .setTitle("Go back home?")
            .setMessage("This will discard the current swipe session and return you to the main page.")
            .setPositiveButton("Go Home") { _, _ ->
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Keep Swiping", null)
            .show()
    }

    companion object {
        const val EXTRA_CITY = "extra_city"
    }
}
