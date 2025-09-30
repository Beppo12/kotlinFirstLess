package com.example.kotlinfirstless

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var db: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meditation1) // убедись, что имя файла совпадает

        db = UserDatabase.getDatabase(this)

        val title = intent.getStringExtra("title") ?: "No title"
        val description = intent.getStringExtra("description") ?: ""

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val imgCover = findViewById<ImageView>(R.id.imgCover)
        val btnAddFavorites = findViewById<Button>(R.id.btnAddFavorites)

        tvTitle.text = title
        tvDescription.text = description
        // imgCover.setImageResource(...) — если картинка есть

        btnAddFavorites.setOnClickListener {
            // Получаем id текущего пользователя из SharedPreferences
            val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val currentUserId = prefs.getInt("current_user_id", -1)
            if (currentUserId == -1) {
                Toast.makeText(this, "Please login to add favorites", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val fav = FavoriteItem(userId = currentUserId, title = title, description = description)
                db.favoriteDao().insert(fav)
                runOnUiThread {
                    Toast.makeText(this@DetailActivity, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
