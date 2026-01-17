package com.example.sgroupmobile2025.musicplayer.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.musicplayer.R
import com.example.sgroupmobile2025.musicplayer.data.Track
import com.example.sgroupmobile2025.musicplayer.databinding.ItemTrackListBinding

class TrackListAdapter(
    private val onClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackListAdapter.ViewHolder>() {

    private val data = mutableListOf<Track>()

    fun submitList(list: List<Track>) {
        Log.e("listday", list.toString())

        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemTrackListBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {

            binding.tvTitle.text = track.title
            binding.tvArtist.text = track.artist.name

            val imageUrl =
                track.album?.coverMedium
                    ?: track.artist.pictureMedium

            Glide.with(binding.root)
                .load(imageUrl)
                .placeholder(R.drawable.ic_music)
                .error(R.drawable.ic_music)
                .into(binding.imgCover)

            binding.root.setOnClickListener {
                onClick(track)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrackListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}
