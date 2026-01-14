package com.example.sgroupmobile2025.musicplayer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.musicplayer.data.Album
import com.example.sgroupmobile2025.musicplayer.databinding.ItemAlbumBinding

class AlbumAdapter(private var albums: List<Album>): RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return(ViewHolder(binding))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.onHolder(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateAlbums(newAlbums: List<Album>){
        albums = newAlbums
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        fun onHolder(album: Album){
            Glide.with(binding.root)
                .load(album.coverMedium)
                .into(binding.imAlbum)
            binding.tvAlbumTitle.text = album.title
        }
    }
}