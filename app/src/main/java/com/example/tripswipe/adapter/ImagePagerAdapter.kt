package com.example.tripswipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripswipe.databinding.ItemImageSlideBinding
import com.example.tripswipe.model.GalleryImage
import com.example.tripswipe.util.loadGalleryImage

class ImagePagerAdapter(
    private val images: List<GalleryImage>
) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(
        private val binding: ItemImageSlideBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: GalleryImage) {
            binding.imageSlide.loadGalleryImage(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageSlideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) = holder.bind(images[position])

    override fun getItemCount(): Int = images.size
}
