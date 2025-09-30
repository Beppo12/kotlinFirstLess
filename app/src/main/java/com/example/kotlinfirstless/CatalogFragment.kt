package com.example.kotlinfirstless

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView

class CatalogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)

        // 🔹 Карточка 1 — Morning Calm
        view.findViewById<CardView>(R.id.card_morning_calm).setOnClickListener {
            openMeditationFragment(
                title = "Morning Calm",
                description = "This 10-minute guided meditation helps you start your day with calm and focus.",
                fragmentClass = Meditation1Fragment::class.java
            )
        }

        // 🔹 Карточка 2 — Deep Sleep
        view.findViewById<CardView>(R.id.card_deep_sleep).setOnClickListener {
            openMeditationFragment(
                title = "Deep Sleep",
                description = "This 10-minute guided meditation will help you release tension and find deep relaxation.",
                fragmentClass = Meditation2Fragment::class.java
            )
        }

        // 🔹 Карточка 3 — Mindfulness
        view.findViewById<CardView>(R.id.card_stress_relief).setOnClickListener {
            openMeditationFragment(
                title = "Mindfulness",
                description = "Focus on the present moment and relieve stress with this 10-minute meditation.",
                fragmentClass = Meditation3Fragment::class.java
            )
        }

        // 🔹 Карточка 4 — Deep Relax
        view.findViewById<CardView>(R.id.card_focus_boost).setOnClickListener {
            openMeditationFragment(
                title = "Deep Relax",
                description = "Relax deeply and release tension with this beginner-friendly guided meditation.",
                fragmentClass = Meditation4Fragment::class.java
            )
        }

        return view
    }

    private fun openMeditationFragment(title: String, description: String, fragmentClass: Class<out Fragment>) {
        val fragment = fragmentClass.newInstance().apply {
            arguments = Bundle().apply {
                putString("title", title)
                putString("description", description)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
