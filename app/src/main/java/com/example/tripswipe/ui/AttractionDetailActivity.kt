package com.example.tripswipe.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.tripswipe.adapter.ImagePagerAdapter
import com.example.tripswipe.databinding.ActivityAttractionDetailBinding
import com.example.tripswipe.model.Attraction

class AttractionDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAttractionDetailBinding
    private lateinit var attraction: Attraction
    private var imageCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttractionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        attraction = readAttraction() ?: run {
            finish()
            return
        }

        val images = attraction.galleryItems
        imageCount = images.size

        binding.textTitle.text = attraction.name
        binding.textSubtitle.text = attraction.city
        binding.textCategory.text = attraction.category
        binding.textDescription.text = attraction.description
        binding.textImageCount.text = "1 / $imageCount"

        binding.imagePager.adapter = ImagePagerAdapter(images)
        binding.imagePager.setCurrentItem(0, false)
        binding.imagePager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.textImageCount.text = "${position + 1} / $imageCount"
                updateArrowState(position)
            }
        })

        binding.buttonImagePrevious.setOnClickListener { moveToPage(-1) }
        binding.buttonImageNext.setOnClickListener { moveToPage(1) }
        binding.buttonWebsite.setOnClickListener { openUrl(attraction.websiteUrl) }
        binding.buttonMap.setOnClickListener { openUrl(attraction.mapUrl) }
        binding.buttonBack.setOnClickListener { finish() }

        updateArrowState(0)
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

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No app found to open this link.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readAttraction(): Attraction? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_ATTRACTION, Attraction::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(EXTRA_ATTRACTION) as? Attraction
        }
    }

    companion object {
        const val EXTRA_ATTRACTION = "extra_attraction"
    }
}
