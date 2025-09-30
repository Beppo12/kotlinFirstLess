package com.example.kotlinfirstless

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var adapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        recyclerView = view.findViewById(R.id.favoritesRecyclerView)
        emptyStateLayout = view.findViewById(R.id.empty_state)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = FavoritesAdapter(emptyList()) { favoriteItem ->
            openMeditationFragment(favoriteItem)
        }

        recyclerView.adapter = adapter

        loadFavorites()

        return view
    }

    private fun loadFavorites() {
        val prefs = requireContext().getSharedPreferences("app_prefs", 0)
        val currentUserId = prefs.getInt("current_user_id", -1)

        if (currentUserId == -1) {
            showEmptyState()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val favorites = UserDatabase.getDatabase(requireContext())
                .favoriteDao()
                .getFavoritesByUser(currentUserId)

            if (favorites.isEmpty()) {
                showEmptyState()
            } else {
                adapter = FavoritesAdapter(favorites) { favoriteItem ->
                    openMeditationFragment(favoriteItem)
                }
                recyclerView.adapter = adapter
                recyclerView.visibility = View.VISIBLE
                emptyStateLayout.visibility = View.GONE
            }
        }
    }

    private fun showEmptyState() {
        recyclerView.visibility = View.GONE
        emptyStateLayout.visibility = View.VISIBLE
    }

    private fun openMeditationFragment(favoriteItem: FavoriteItem) {
        val fragment = when (favoriteItem.title) {
            "Morning Calm" -> Meditation1Fragment()
            "Deep Sleep" -> Meditation2Fragment()
            "Mindfulness" -> Meditation3Fragment()
            "Deep Relax" -> Meditation4Fragment()
            else -> Meditation1Fragment()
        }

        fragment.arguments = Bundle().apply {
            putString("title", favoriteItem.title)
            putString("description", favoriteItem.description)
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
