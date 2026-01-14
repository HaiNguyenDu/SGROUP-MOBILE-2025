package com.example.sgroupmobile2025.musicplayer.repository

import com.example.sgroupmobile2025.musicplayer.api.ApiClient
import com.example.sgroupmobile2025.musicplayer.data.Album
import com.example.sgroupmobile2025.musicplayer.data.Artist
import com.example.sgroupmobile2025.musicplayer.data.Track

class MusicRepository {
    suspend fun getTopTracks(): List<Track> = ApiClient.api.getTopTracks().data
    suspend fun getTopAlbums(): List<Album> = ApiClient.api.getTopAlbums().data
    suspend fun getTopArtists(): List<Artist> = ApiClient.api.getTopArtists().data
}