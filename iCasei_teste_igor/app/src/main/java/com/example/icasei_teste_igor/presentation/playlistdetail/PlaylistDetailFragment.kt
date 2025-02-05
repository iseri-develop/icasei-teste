package com.example.icasei_teste_igor.presentation.playlistdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icasei_teste_igor.data.local.AppDatabase
import com.example.icasei_teste_igor.databinding.FragmentPlaylistDetailBinding
import com.example.icasei_teste_igor.presentation.playlistdetail.adapter.PlaylistVideoAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PlaylistDetailFragment : Fragment() {

    private val args: PlaylistDetailFragmentArgs by navArgs()

    private var _binding: FragmentPlaylistDetailBinding? = null
    private val binding get() = _binding!!

    private val playlistDao by lazy { AppDatabase.getDatabase(requireContext()).playlistDao() }
    private val viewModel: PlaylistDetailViewModel by viewModels {
        PlaylistDetailViewModelFactory(playlistDao)
    }

    private var playlistId: Long = -1
    private var playlistTitle: String = ""

    private lateinit var adapter: PlaylistVideoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        playlistId = args.playlistId
        playlistTitle = args.playlistTitle

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        viewModel.loadVideos(playlistId)

        binding.textViewPlaylistTitle.text = args.playlistTitle
        binding.buttonDeletePlaylist.setOnClickListener {
            viewModel.deletePlaylist(playlistId)
            Toast.makeText(requireContext(), "Playlist excluída", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        adapter = PlaylistVideoAdapter(emptyList()) { video ->
            viewModel.removeVideoFromPlaylist(playlistId, video.videoId)
        }
        binding.recyclerViewVideos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewVideos.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.videos.collectLatest { videos ->
                adapter = PlaylistVideoAdapter(videos) { video ->
                    viewModel.removeVideoFromPlaylist(playlistId, video.videoId)
                }
                binding.recyclerViewVideos.adapter = adapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

