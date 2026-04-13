package com.example.tripswipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripswipe.databinding.ItemCityBinding
import com.example.tripswipe.model.City
import com.example.tripswipe.model.GalleryImage
import com.example.tripswipe.util.loadGalleryImage

class CityAdapter(
    private var cities: List<City>,
    private val onClick: (City) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    inner class CityViewHolder(private val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(city: City) {
            binding.imageCity.loadGalleryImage(
                GalleryImage(
                    remoteUrl = city.imageUrl,
                    localResId = city.fallbackImageRes
                )
            )
            binding.textCityName.text = city.name
            binding.textCitySubtitle.text = city.subtitle
            binding.root.setOnClickListener { onClick(city) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) = holder.bind(cities[position])

    override fun getItemCount(): Int = cities.size

    fun submitList(newCities: List<City>) {
        cities = newCities
        notifyDataSetChanged()
    }
}
