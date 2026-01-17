package com.example.sgroupmobile2025.musicplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgroupmobile2025.musicplayer.data.*
import com.example.sgroupmobile2025.musicplayer.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MusicViewModel : ViewModel() {

    private val repo = MusicRepository()

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: StateFlow<List<Track>> = _tracks

    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums

    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists: StateFlow<List<Artist>> = _artists


    private val _detailTracks = MutableStateFlow<List<Track>>(emptyList())
    val detailTracks: StateFlow<List<Track>> = _detailTracks


    fun loadHomeData() {
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

    fun loadTracksByAlbum(albumId: Long) {
        viewModelScope.launch {
            try {
                _detailTracks.value = repo.getTracksByAlbum(albumId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadTracksByArtist(artistId: Long) {
        viewModelScope.launch {
            try {
                _detailTracks.value = repo.getTracksByArtist(artistId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
