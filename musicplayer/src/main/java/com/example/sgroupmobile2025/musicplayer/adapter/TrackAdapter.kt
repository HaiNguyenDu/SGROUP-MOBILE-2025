package com.example.sgroupmobile2025.musicplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.musicplayer.data.Track
import com.example.sgroupmobile2025.musicplayer.databinding.ItemMusicBinding

class TrackAdapter(
    private var tracks: List<Track>,
    private val onPlayClick: (Int) -> Unit
) : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMusicBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, onPlayClick)
    }

    override fun getItemCount(): Int = tracks.size

    fun updateTracks(newTracks: List<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemMusicBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, onPlayClick: (Int) -> Unit) {
            binding.tvMusicTitle.text = tracks[position].title
            binding.tvArtistName.text = tracks[position].artist.name

            Glide.with(binding.root)
                .load(tracks[position].album.coverMedium)
                .into(binding.ivAlbumArt)
            binding.tvDuration.text = tracks[position].duration.toString()

            binding.root.setOnClickListener {
                onPlayClick(position)
            }
        }
    }
}
