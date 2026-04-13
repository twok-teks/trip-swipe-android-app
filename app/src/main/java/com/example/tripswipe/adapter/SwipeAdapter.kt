package com.example.tripswipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.tripswipe.databinding.ItemSwipeCardBinding
import com.example.tripswipe.model.Attraction

class SwipeAdapter(
    private val items: MutableList<Attraction>,
    private val onAttractionClick: (Attraction) -> Unit
) : RecyclerView.Adapter<SwipeAdapter.SwipeViewHolder>() {

    inner class SwipeViewHolder(private val binding: ItemSwipeCardBinding) : RecyclerView.ViewHolder(binding.root) {
        private var imageCount = 0

        init {
            binding.imagePager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.textImageCount.text = "${position + 1} / $imageCount"
                    updateArrowState(position)
                }
            })
        }

        fun bind(attraction: Attraction) {
            val images = attraction.galleryItems
            imageCount = images.size
            binding.imagePager.adapter = ImagePagerAdapter(images)
            binding.imagePager.offscreenPageLimit = 1
            binding.imagePager.setCurrentItem(0, false)
            binding.textImageCount.text = "1 / ${images.size}"
            updateArrowState(0)
            binding.textCategory.text = attraction.category
            binding.textName.text = attraction.name
            binding.textDescription.text = attraction.description
            binding.buttonImagePrevious.setOnClickListener { moveToPage(-1) }
            binding.buttonImageNext.setOnClickListener { moveToPage(1) }
            binding.buttonViewDetails.setOnClickListener { onAttractionClick(attraction) }
            binding.root.setOnClickListener { onAttractionClick(attraction) }
        }

        private fun moveToPage(offset: Int) {
            val nextPage = binding.imagePager.currentItem + offset
            if (nextPage in 0 until imageCount) {
                binding.imagePager.setCurrentItem(nextPage, true)
            }
        }

        private fun updateArrowState(currentPage: Int) {
            val hasMultipleImages = imageCount > 1
            binding.buttonImagePrevious.isEnabled = hasMultipleImages && currentPage > 0
            binding.buttonImageNext.isEnabled = hasMultipleImages && currentPage < imageCount - 1
            binding.buttonImagePrevious.alpha = if (binding.buttonImagePrevious.isEnabled) 1f else 0.35f
            binding.buttonImageNext.alpha = if (binding.buttonImageNext.isEnabled) 1f else 0.35f
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeViewHolder {
        val binding = ItemSwipeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SwipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SwipeViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    fun getItem(position: Int): Attraction = items[position]

    fun removeAt(position: Int): Attraction = items.removeAt(position)

    fun submitItems(newItems: List<Attraction>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
