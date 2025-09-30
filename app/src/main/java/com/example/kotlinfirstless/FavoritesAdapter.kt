package com.example.kotlinfirstless

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class FavoritesAdapter(
    private val items: List<FavoriteItem>,
    private val onClick: (FavoriteItem) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.tvTitleItem)
        val subtitleTextView: TextView = view.findViewById(R.id.tvSubtitleItem)
        val card: CardView = view.findViewById(R.id.cardFavorite) // <- убедись, что в item_favorite.xml есть id cardFavorite
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = items[position]
        holder.titleTextView.text = item.title
        holder.subtitleTextView.text = item.description
        holder.card.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
