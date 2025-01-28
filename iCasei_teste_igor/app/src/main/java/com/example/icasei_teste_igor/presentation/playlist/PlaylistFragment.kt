package com.example.icasei_teste_igor.presentation.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.icasei_teste_igor.databinding.FragmentPlaylistBinding

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val playlistViewModel =
            ViewModelProvider(this)[PlaylistViewModel::class.java]

        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textPlaylist
        playlistViewModel.channel.observe(viewLifecycleOwner) {
            if (it != null && it.items.isNotEmpty()) {
                it.items.forEach { item ->
                    textView.text = item.snippet.title
                }
            } else {
                textView.text = "No channel"
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}