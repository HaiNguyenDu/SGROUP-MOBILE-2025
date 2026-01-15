package com.example.sgroupmobile2025.musicplayer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgroupmobile2025.musicplayer.data.Album
import com.example.sgroupmobile2025.musicplayer.data.Artist
import com.example.sgroupmobile2025.musicplayer.data.Track
import com.example.sgroupmobile2025.musicplayer.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MusicViewModel: ViewModel() {
    private val repo = MusicRepository()
    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val track: StateFlow<List<Track>> = _tracks
    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums
    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists: StateFlow<List<Artist>> = _artists

    fun loadData() {
        viewModelScope.launch {
            try {
                _tracks.value = repo.getTopTracks()
                _albums.value = repo.getTopAlbums()
                _artists.value = repo.getTopArtists()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}