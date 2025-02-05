package com.example.icasei_teste_igor.presentation.home

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.icasei_teste_igor.databinding.DialogSelectPlaylistBinding
import com.example.icasei_teste_igor.domain.model.Snippet
import com.example.icasei_teste_igor.domain.model.ThumbnailsYT
import com.example.icasei_teste_igor.domain.model.VideoYT
import com.example.icasei_teste_igor.presentation.playlist.PlaylistViewModel
import kotlinx.coroutines.flow.collectLatest

class SelectPlaylistDialogFragment : DialogFragment() {

    private var _binding: DialogSelectPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PlaylistViewModel

    private var selectedVideo: VideoYT.VideoYTItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedVideo = VideoYT.VideoYTItem(
                videoId = it.getString(ARG_VIDEO_ID) ?: "",
                snippet = Snippet(
                    title = it.getString(ARG_VIDEO_TITLE) ?: "",
                    description = "",
                    customUrl = "",
                    publishedAt = "",
                    thumbnails = ThumbnailsYT(
                        ThumbnailsYT.High(
                            url = it.getString(ARG_VIDEO_THUMBNAIL_URL) ?: ""
                        )
                    ),
                    country = ""
                )
            )
        }

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogSelectPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        setupListeners()
    }

    private fun setupSpinner() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.playlists.collectLatest { playlists ->
                val adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.simple_spinner_dropdown_item,
                    playlists.map { it.title }
                )
                binding.spinnerPlaylists.adapter = adapter
            }
        }
    }

    private fun setupListeners() {
        binding.buttonAddVideo.setOnClickListener {
            val selectedPosition = binding.spinnerPlaylists.selectedItemPosition
            val selectedPlaylist = viewModel.playlists.value.getOrNull(selectedPosition)

            val videoId = selectedVideo?.videoId?.let { id ->
                when (id) {
                    is String -> id
                    is Map<*, *> -> (id as? Map<*, *>)?.get("videoId") as? String
                    else -> null
                }
            }

            if (selectedPlaylist != null && videoId != null) {
                viewModel.addVideoToPlaylist(selectedPlaylist.id, selectedVideo!!)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Erro ao adicionar vídeo", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setViewModel(playlistViewModel: PlaylistViewModel) {
        viewModel = playlistViewModel
    }

    companion object {
        private const val ARG_VIDEO_ID = "ARG_VIDEO_ID"
        private const val ARG_VIDEO_TITLE = "ARG_VIDEO_TITLE"
        private const val ARG_VIDEO_THUMBNAIL_URL = "ARG_VIDEO_THUMBNAIL_URL"

        fun newInstance(
            videoId: String,
            title: String,
            thumbnailUrl: String
        ): SelectPlaylistDialogFragment {
            val args = Bundle().apply {
                putString(ARG_VIDEO_ID, videoId)
                putString(ARG_VIDEO_TITLE, title)
                putString(ARG_VIDEO_THUMBNAIL_URL, thumbnailUrl)
            }
            return SelectPlaylistDialogFragment().apply {
                arguments = args
            }
        }
    }
}
