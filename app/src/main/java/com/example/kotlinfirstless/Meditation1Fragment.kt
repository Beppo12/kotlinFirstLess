package com.example.kotlinfirstless

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class Meditation1Fragment : Fragment() {

    private lateinit var db: UserDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.meditation1, container, false)

        db = UserDatabase.getDatabase(requireContext())

        val title = arguments?.getString("title") ?: "Morning Calm"
        val description = arguments?.getString("description") ?: "Start your day with a calm mind and relaxed body."

        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        val btnAddFavorites = view.findViewById<Button>(R.id.btnAddFavorites)

        tvTitle.text = title
        tvDescription.text = description

        btnAddFavorites.setOnClickListener {
            val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val currentUserId = prefs.getInt("current_user_id", -1)
            if (currentUserId == -1) {
                Toast.makeText(requireContext(), "Please login to add favorites", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val existing = db.favoriteDao().getByUserAndTitle(currentUserId, title)
                if (existing == null) {
                    val fav = FavoriteItem(userId = currentUserId, title = title, description = description)
                    db.favoriteDao().insert(fav)
                    Toast.makeText(requireContext(), "Added to favorites!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Already in favorites", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }
}
