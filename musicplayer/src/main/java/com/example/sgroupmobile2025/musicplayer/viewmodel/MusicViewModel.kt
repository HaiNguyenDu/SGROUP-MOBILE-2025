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

    private val _currentTitle = MutableStateFlow<String>("")
    val currentTitle: StateFlow<String> = _currentTitle
    private val _currentArtist = MutableStateFlow<String>("")
    val currentArtist: StateFlow<String> = _currentArtist

    private val _isPlay = MutableStateFlow<Boolean>(false)
    val isPlay: StateFlow<Boolean> = _isPlay

    private val _currentImage = MutableStateFlow<String>("")
    val currentImage = _currentImage

    fun updateTitle(newTitle: String){
        _currentTitle.value = newTitle
    }
    fun updateArtist(newArtist: String) {
        _currentArtist.value = newArtist
    }

    fun updatePlayState(isPlaying: Boolean) {
        _isPlay.value = isPlaying
    }

    fun updateImage(newImg: String){
        _currentImage.value = newImg
    }


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
