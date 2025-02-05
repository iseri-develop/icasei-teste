package com.example.icasei_teste_igor.presentation.playlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.icasei_teste_igor.R
import com.google.firebase.auth.FirebaseAuth

class CreatePlaylistDialog(private val context: Context, private val onPlaylistCreated: (String) -> Unit) :
    DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_create_playlist, container, false)
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        // Verificar se o usuário está logado
        if (currentUser == null) {
            Toast.makeText(
                requireContext(),
                "Você precisa estar logado para criar playlists!",
                Toast.LENGTH_SHORT
            ).show()

            // Fechar o diálogo após o aviso
            dismiss()
        }

        val editTextTitle = view.findViewById<EditText>(R.id.editTexttPlaylistName)
        val buttonCreate = view.findViewById<Button>(R.id.btnCreate)
        val buttonCancel = view.findViewById<Button>(R.id.btnCancel)

        buttonCreate.setOnClickListener {
            val title = editTextTitle.text.toString().trim()

            if (title.isNotEmpty() && currentUser != null) {
                onPlaylistCreated(title)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Erro ao criar playlist.", Toast.LENGTH_SHORT).show()
            }
        }

        buttonCancel.setOnClickListener {
            dismiss()
        }
    }
}
