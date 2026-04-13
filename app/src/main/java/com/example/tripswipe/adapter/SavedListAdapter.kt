package com.example.tripswipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripswipe.databinding.ItemSavedListBinding
import com.example.tripswipe.model.SavedItinerary

class SavedListAdapter(
    private var items: List<SavedItinerary>,
    private val deletedMode: Boolean = false,
    private val onClick: (SavedItinerary) -> Unit,
    private val onActionClick: (SavedItinerary) -> Unit
) : RecyclerView.Adapter<SavedListAdapter.SavedListViewHolder>() {

    inner class SavedListViewHolder(private val binding: ItemSavedListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SavedItinerary) {
            binding.textListName.text = item.name
            val stopCount = if (deletedMode) item.attractions.size + item.deletedAttractions.size else item.attractions.size
            binding.textListMeta.text = "${item.city} • $stopCount stops"
            binding.textListDate.text = if (deletedMode) {
                item.deletedAtLabel?.let { "Deleted $it" } ?: item.createdAtLabel
            } else {
                item.createdAtLabel
            }
            binding.buttonListAction.text = if (deletedMode) "Restore" else "Delete"
            binding.buttonListAction.setOnClickListener { onActionClick(item) }
            binding.root.setOnClickListener {
                if (!deletedMode) onClick(item)
            }
            binding.root.isClickable = !deletedMode
            binding.textTapHint.visibility = if (deletedMode) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedListViewHolder {
        val binding = ItemSavedListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedListViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<SavedItinerary>) {
        items = newItems
        notifyDataSetChanged()
    }
}
