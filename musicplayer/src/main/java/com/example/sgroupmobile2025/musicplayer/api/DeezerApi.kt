package com.example.sgroupmobile2025.musicplayer.api

import com.example.sgroupmobile2025.musicplayer.data.AlbumResponse
import com.example.sgroupmobile2025.musicplayer.data.ArtistResponse
import com.example.sgroupmobile2025.musicplayer.data.TrackResponse
import retrofit2.http.GET

interface DeezerApi {

    @GET("chart/0/tracks")
    suspend fun getTopTracks(): TrackResponse

    @GET("chart/0/albums")
    suspend fun getTopAlbums(): AlbumResponse

    @GET("chart/0/artists")
    suspend fun getTopArtists(): ArtistResponse
}
