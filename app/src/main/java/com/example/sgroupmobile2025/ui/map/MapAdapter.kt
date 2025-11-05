package com.example.sgroupmobile2025.ui.map

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sgroupmobile2025.data.model.AutoCompleteResponse
import com.example.sgroupmobile2025.data.model.Prediction
import com.example.sgroupmobile2025.databinding.MapItemBinding

class MapAdapter(private var places: List<Prediction>): RecyclerView.Adapter<MapAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = MapItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.onHolder(places[position])
    }

    override fun getItemCount(): Int = places.size
    @SuppressLint("NotifyDataSetChanged")
    fun updatePlaces(newPlaces: List<Prediction>){
        places = newPlaces
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: MapItemBinding): RecyclerView.ViewHolder(binding.root){
        fun onHolder(place: Prediction){
            binding.tvPlaceName.text = place.description
        }
    }
}