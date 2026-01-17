package com.example.sgroupmobile2025.musicplayer.api

import com.example.sgroupmobile2025.musicplayer.data.AlbumDetailResponse
import com.example.sgroupmobile2025.musicplayer.data.AlbumResponse
import com.example.sgroupmobile2025.musicplayer.data.ArtistDetailResponse
import com.example.sgroupmobile2025.musicplayer.data.ArtistResponse
import com.example.sgroupmobile2025.musicplayer.data.TrackResponse
import retrofit2.http.GET

interface DeezerApi {

    // ====== CHART ======
    @GET("chart/0/tracks")
    suspend fun getTopTracks(): TrackResponse

    @GET("chart/0/albums")
    suspend fun getTopAlbums(): AlbumResponse

    @GET("chart/0/artists")
    suspend fun getTopArtists(): ArtistResponse


    // ====== ALBUM DETAIL ======
    @GET("album/{id}")
    suspend fun getAlbumDetail(
        @retrofit2.http.Path("id") albumId: Long
    ): AlbumDetailResponse

    @GET("album/{id}/tracks")
    suspend fun getTracksByAlbum(
        @retrofit2.http.Path("id") albumId: Long
    ): TrackResponse


    // ====== ARTIST DETAIL ======
    @GET("artist/{id}")
    suspend fun getArtistDetail(
        @retrofit2.http.Path("id") artistId: Long
    ): ArtistDetailResponse

    @GET("artist/{id}/top?limit=50")
    suspend fun getTopTracksByArtist(
        @retrofit2.http.Path("id") artistId: Long
    ): TrackResponse
}
