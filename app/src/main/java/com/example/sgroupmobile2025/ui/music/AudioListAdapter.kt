package com.example.sgroupmobile2025.ui.music

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sgroupmobile2025.databinding.ItemAudioBinding

class AudioListAdapter(
    private val audioList: List<String>,
    private val currentIndex: Int,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<AudioListAdapter.AudioViewHolder>() {

    inner class AudioViewHolder(private val binding: ItemAudioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(audioPath: String, index: Int, isPlaying: Boolean) {
            val fileName = audioPath.substringAfterLast("/")
            binding.tvAudioTitle.text = fileName
            binding.tvAudioIndex.text = (index + 1).toString()
            binding.tvAudioPath.text = audioPath

            if (isPlaying) {
                binding.root.setBackgroundColor(
                    itemView.context.getColor(
                        android.R.color.darker_gray
                    )
                )
            } else {
                binding.root.setBackgroundColor(
                    itemView.context.getColor(
                        android.R.color.white
                    )
                )
            }

            binding.root.setOnClickListener {
                onItemClick(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val binding = ItemAudioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AudioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.bind(audioList[position], position, position == currentIndex)
    }

    override fun getItemCount(): Int = audioList.size
}