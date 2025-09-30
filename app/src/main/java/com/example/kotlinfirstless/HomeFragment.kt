package com.example.kotlinfirstless

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Клик по иконке профиля — открываем экран авторизации/регистрации
        val ivProfile = view.findViewById<ImageView>(R.id.ivProfile)
        ivProfile?.setOnClickListener {
            startActivity(Intent(requireContext(), AuthActivity::class.java))
        }

        // Клик по первой карточке каталога — открыть DetailActivity
        val card = view.findViewById<CardView>(R.id.card_morning_calm)
        card?.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("title", "Morning Calm")
            intent.putExtra("description", "This 10-minute guided meditation will help you release tension and find deep relaxation.")
            startActivity(intent)
        }
    }
}
