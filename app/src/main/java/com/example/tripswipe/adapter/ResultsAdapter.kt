package com.example.tripswipe.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.tripswipe.databinding.ItemResultBinding
import com.example.tripswipe.model.Attraction

class ResultsAdapter(
    private var items: List<Attraction>,
    private val onMoveClick: (Attraction) -> Unit
) : RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

    var showDeletedMode: Boolean = false

    inner class ResultViewHolder(private val binding: ItemResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(attraction: Attraction) {
            val images = attraction.galleryItems
            binding.imagePager.adapter = ImagePagerAdapter(images)
            binding.imagePager.offscreenPageLimit = 1
            binding.imagePager.setCurrentItem(0, false)
            binding.textImageCount.text = "1 / ${images.size}"
            binding.imagePager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.textImageCount.text = "${position + 1} / ${images.size}"
                }
            })

            binding.textName.text = attraction.name
            binding.textCategory.text = attraction.category
            binding.textDescription.text = attraction.description
            binding.buttonMove.text = if (showDeletedMode) "Restore" else "\uD83D\uDDD1\uFE0F"

            binding.buttonWebsite.setOnClickListener { openUrl(attraction.websiteUrl) }
            binding.buttonMap.setOnClickListener { openUrl(attraction.mapUrl) }
            binding.buttonMove.setOnClickListener { onMoveClick(attraction) }
        }

        private fun openUrl(url: String) {
            val context = binding.root.context
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "No app found to open this link.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<Attraction>, deletedMode: Boolean = showDeletedMode) {
        items = newItems
        showDeletedMode = deletedMode
        notifyDataSetChanged()
    }
}
