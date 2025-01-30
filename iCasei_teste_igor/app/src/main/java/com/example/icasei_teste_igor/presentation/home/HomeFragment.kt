package com.example.icasei_teste_igor.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icasei_teste_igor.R
import com.example.icasei_teste_igor.databinding.FragmentHomeBinding
import com.example.icasei_teste_igor.presentation.home.adapter.HomeAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeVewModel: HomeViewModel by viewModels()

    private val adapter = HomeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewHome.adapter = adapter
        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())

        menuConfig()
        observe()
    }

    private fun menuConfig() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items
                menuInflater.inflate(R.menu.menu_search, menu)

                val searchView = menu.findItem(R.id.menu_search).actionView as SearchView
                searchView.queryHint = getString(R.string.search_hint)

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (!query.isNullOrEmpty()) {
                            homeVewModel.getVideoSearched(query)
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun observe() {
        homeVewModel.video.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.textViewHome.visibility = View.GONE
                adapter.setData(it.items)
            }
        }

        homeVewModel.videoSearched.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.textViewHome.visibility = View.GONE
                adapter.setData(it.items)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}