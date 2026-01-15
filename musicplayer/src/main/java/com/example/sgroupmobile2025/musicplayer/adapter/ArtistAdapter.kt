package com.example.sgroupmobile2025.musicplayer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.musicplayer.data.Artist
import com.example.sgroupmobile2025.musicplayer.databinding.ItemArtistBinding

class ArtistAdapter(
    private var artists: List<Artist>,
    private val onClick: (Artist) -> Unit
): RecyclerView.Adapter<ArtistAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return(ViewHolder(binding))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.onHolder(artists[position])
    }

    override fun getItemCount(): Int = artists.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateArtist(newArtists: List<Artist>){
        artists = newArtists
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemArtistBinding): RecyclerView.ViewHolder(binding.root){
        fun onHolder(artist: Artist){
            Glide.with(binding.root)
                .load(artist.pictureMedium)
                .into(binding.imArtist)
            binding.tvArtistName.text = artist.name
            binding.root.setOnClickListener {
                onClick(artist)
            }
        }
    }
}