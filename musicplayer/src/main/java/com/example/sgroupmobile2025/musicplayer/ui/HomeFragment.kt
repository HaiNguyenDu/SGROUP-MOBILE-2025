package com.example.sgroupmobile2025.musicplayer.ui

import android.content.*
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.*
import com.example.sgroupmobile2025.musicplayer.adapter.*
import com.example.sgroupmobile2025.musicplayer.constants.MusicAction
import com.example.sgroupmobile2025.musicplayer.databinding.FragmentHomeBinding
import com.example.sgroupmobile2025.musicplayer.service.MusicService
import com.example.sgroupmobile2025.musicplayer.viewmodel.MusicViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    private val trackAdapter = TrackAdapter(emptyList()) { index ->
        val intent = Intent(MusicAction.ACTION_PLAY_BY_INDEX).apply {
            putExtra(MusicAction.EXTRA_INDEX, index)
        }

        LocalBroadcastManager
            .getInstance(requireContext())
            .sendBroadcast(intent)
    }

    private val albumAdapter = AlbumAdapter(emptyList())
    private val artistAdapter = ArtistAdapter(emptyList())

    private val viewModel: MusicViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // BẮT BUỘC start service trước
        val serviceIntent = Intent(requireContext(), MusicService::class.java)
        requireContext().startService(serviceIntent)

        setUpRecyclerView()
        viewModel.loadData()
        observeData()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.track.collect { list ->
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

    private fun sendPlaylistToService(list: List<com.example.sgroupmobile2025.musicplayer.data.Track>) {
        val intent = Intent(MusicAction.ACTION_SET_PLAYLIST).apply {
            putParcelableArrayListExtra(
                MusicAction.EXTRA_PLAYLIST,
                ArrayList(list)
            )
        }

        LocalBroadcastManager
            .getInstance(requireContext())
            .sendBroadcast(intent)
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
}
