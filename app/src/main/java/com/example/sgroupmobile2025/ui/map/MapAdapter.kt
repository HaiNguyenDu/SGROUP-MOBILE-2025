package com.example.sgroupmobile2025.ui.map

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.data.model.Prediction
import com.example.sgroupmobile2025.databinding.MapItemBinding

class MapAdapter(
    private var places: List<Prediction>,
    private val onCLick: (String, String) -> Unit
) : RecyclerView.Adapter<MapAdapter.ViewHolder>() {
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
        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        holder.onHolder(places[position])
    }

    override fun getItemCount(): Int = places.size

    @SuppressLint("NotifyDataSetChanged")
    fun updatePlaces(newPlaces: List<Prediction>) {
        places = newPlaces
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: MapItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onHolder(place: Prediction) {
            binding.tvPlaceName.text = place.description
            itemView.setOnClickListener {
                onCLick(place.placeId, place.description)
            }
        }
    }
}