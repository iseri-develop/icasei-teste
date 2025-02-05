package com.example.icasei_teste_igor.presentation.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icasei_teste_igor.data.local.AppDatabase
import com.example.icasei_teste_igor.databinding.FragmentPlaylistBinding
import com.example.icasei_teste_igor.domain.model.Playlist
import com.example.icasei_teste_igor.presentation.playlist.adapter.PlaylistAdapter
import com.example.icasei_teste_igor.presentation.playlistdetail.PlaylistViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val playlistDao by lazy { AppDatabase.getDatabase(requireContext()).playlistDao() }
    private val viewModel: PlaylistViewModel by viewModels { PlaylistViewModelFactory(playlistDao) }

    private lateinit var playlistAdapter: PlaylistAdapter
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupCreatePlaylistButton()
    }

    private fun setupRecyclerView() {
        playlistAdapter = PlaylistAdapter { playlist -> openPlaylist(playlist) }
        binding.recyclerViewPlaylists.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playlistAdapter
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.user.collect { user ->
                if (user != null) {
                    binding.recyclerViewPlaylists.visibility = View.VISIBLE
                } else {
                    binding.recyclerViewPlaylists.visibility = View.INVISIBLE
                }
            }
        }

        lifecycleScope.launch {
            viewModel.playlists.collect { playlists ->
                playlistAdapter.submitList(playlists)
            }
        }
    }

    private fun setupCreatePlaylistButton() {
        binding.buttonCreatePlaylist.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                CreatePlaylistDialog(requireContext()) { title ->
                    viewModel.createPlaylist(title)
                }.show(parentFragmentManager, "createPlaylistDialog")
            } else {
                Toast.makeText(requireContext(), "Faça login para criar playlists", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openPlaylist(playlist: Playlist) {
        // Navegue para a tela de detalhes da playlist
        val action = PlaylistFragmentDirections
            .actionPlaylistFragmentToPlaylistDetailFragment(playlist.id, playlist.title)

        findNavController().navigate(action)

    }

    override fun onResume() {
        super.onResume()

        viewModel.loadUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
