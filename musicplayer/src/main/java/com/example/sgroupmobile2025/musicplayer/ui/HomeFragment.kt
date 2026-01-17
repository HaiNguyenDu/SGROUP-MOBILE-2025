package com.example.sgroupmobile2025.musicplayer.ui

import android.content.*
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.example.sgroupmobile2025.musicplayer.adapter.*
import com.example.sgroupmobile2025.musicplayer.constants.MusicAction
import com.example.sgroupmobile2025.musicplayer.data.Album
import com.example.sgroupmobile2025.musicplayer.data.Artist
import com.example.sgroupmobile2025.musicplayer.data.Track
import com.example.sgroupmobile2025.musicplayer.databinding.FragmentHomeBinding
import com.example.sgroupmobile2025.musicplayer.service.MusicService
import com.example.sgroupmobile2025.musicplayer.viewmodel.MusicViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val trackAdapter = TrackAdapter(emptyList()) { index ->
        val intent = Intent(MusicAction.ACTION_PLAY_BY_INDEX).apply {
            putExtra(MusicAction.EXTRA_INDEX, index)
            setPackage(requireContext().packageName)
        }
        updateData(index)
        requireContext().sendBroadcast(intent)
    }

    private val albumAdapter = AlbumAdapter(emptyList()){ album ->
        openAlbumDetail(album)
    }
    private val artistAdapter = ArtistAdapter(emptyList()){ artist ->
        openArtistDetail(artist)
    }
    private val viewModel: MusicViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val serviceIntent = Intent(requireContext(), MusicService::class.java)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            requireContext().startForegroundService(serviceIntent)
        } else{
            requireContext().startService(serviceIntent)
        }
        setUpRecyclerView()
        viewModel.loadHomeData()
        observeData()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.tracks.collect { list ->
                        trackAdapter.updateTracks(list)
                        sendPlaylistToService(list)
                    }
                }

                launch {
                    viewModel.albums.collect { list ->
                        albumAdapter.updateAlbums(list)
                    }
                }

                launch {
                    viewModel.artists.collect { list ->
                        artistAdapter.updateArtist(list)
                    }
                }
            }
        }
    }
    fun updateData(index: Int){
        val track = viewModel.tracks.value[index]
        viewModel.updateArtist(track.artist.name)
        viewModel.updateTitle(track.title)
        viewModel.updatePlayState(true)
        viewModel.updateImage(track.album.coverMedium)
    }

    private fun sendPlaylistToService(list: List<Track>) {
        val intent = Intent(MusicAction.ACTION_SET_PLAYLIST).apply {
            putParcelableArrayListExtra(
                MusicAction.EXTRA_PLAYLIST,
                ArrayList(list)
            )
            setPackage(requireContext().packageName)
        }
        requireContext().sendBroadcast(intent)
    }

    private fun setUpRecyclerView() {
        binding.rvMusic.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMusic.adapter = trackAdapter

        binding.rvArtist.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)
        binding.rvArtist.adapter = artistAdapter

        binding.rvAlbum.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvAlbum.adapter = albumAdapter
    }
    private fun openAlbumDetail(album: Album) {
        val intent = Intent(requireContext(), MainDetailList::class.java).apply {
            putExtra("TYPE", "ALBUM")
            putExtra("ID", album.id)
            putExtra("TITLE", album.title)
            putExtra("SUB", "Album")
            putExtra("IMAGE", album.coverMedium)
        }
        startActivity(intent)
    }
    private fun openArtistDetail(artist: Artist) {
        val intent = Intent(requireContext(), MainDetailList::class.java).apply {
            putExtra("TYPE", "ARTIST")
            putExtra("ID", artist.id)
            putExtra("TITLE", artist.name)
            putExtra("SUB", "Nghệ sĩ")
            putExtra("IMAGE", artist.pictureMedium)
        }
        startActivity(intent)
    }


}
