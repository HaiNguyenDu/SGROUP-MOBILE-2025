package com.example.sgroupmobile2025.musicplayer.repository

import com.example.sgroupmobile2025.musicplayer.api.ApiClient
import com.example.sgroupmobile2025.musicplayer.data.Album
import com.example.sgroupmobile2025.musicplayer.data.Artist
import com.example.sgroupmobile2025.musicplayer.data.Track

class MusicRepository {
    suspend fun getTopTracks(): List<Track> = ApiClient.api.getTopTracks().data
    suspend fun getTopAlbums(): List<Album> = ApiClient.api.getTopAlbums().data
    suspend fun getTopArtists(): List<Artist> = ApiClient.api.getTopArtists().data

    suspend fun getTracksByAlbum(albumId: Long): List<Track> =
        ApiClient.api.getTracksByAlbum(albumId).data

    suspend fun getTracksByArtist(artistId: Long): List<Track> =
        ApiClient.api.getTopTracksByArtist(artistId).data
}