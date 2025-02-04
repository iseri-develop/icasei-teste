package com.example.icasei_teste_igor.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.icasei_teste_igor.R
import com.example.icasei_teste_igor.data.local.AppDatabase
import com.example.icasei_teste_igor.databinding.FragmentProfileBinding
import com.example.icasei_teste_igor.presentation.profile.adapter.ProfileAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels() {
        FavoriteVideoViewModelFactory(AppDatabase.getDatabase(requireContext()).favoriteVideoDao())
    }

    private lateinit var favoritesAdapter: ProfileAdapter
    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            profileViewModel.login()
                        }
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGoogleSignIn()
        setupRecyclerView()
        setupListeners()
        setupObservers()
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            profileViewModel.user.collect { user ->
                if (user != null) {
                    // Carregar imagem do perfil com Glide ou Coil
                    user.photoUrl?.let { url ->
                        Glide.with(this@ProfileFragment)
                            .load(url)
                            .placeholder(R.drawable.profile) // Imagem padrão
                            .circleCrop()
                            .into(binding.profileImageView)
                    }

                    binding.nameTextView.text = user.name
                    binding.emailTextView.text = user.email

                    binding.profileContainer.visibility = View.VISIBLE
                    binding.noProfileImageView.visibility = View.GONE
                    binding.loginButton.visibility = View.GONE
                    binding.logoutButton.visibility = View.VISIBLE

                    profileViewModel.loadFavorites()
                } else {
                    binding.profileContainer.visibility = View.GONE
                    binding.noProfileImageView.visibility = View.VISIBLE
                    binding.loginButton.visibility = View.VISIBLE
                    binding.logoutButton.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            profileViewModel.favoriteVideos.collect { favorites ->
                favoritesAdapter.updateFavorites(favorites)
            }
        }
    }

    private fun setupRecyclerView() {
        favoritesAdapter = ProfileAdapter(mutableListOf())
        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoritesAdapter
        }
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            signInWithGoogle()
        }

        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            googleSignInClient.signOut()
            profileViewModel.logout()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}