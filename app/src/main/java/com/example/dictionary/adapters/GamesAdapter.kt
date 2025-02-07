package com.example.dictionary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary.databinding.ItemGameCardBinding
import com.example.dictionary.models.Game

class GamesAdapter(
    private val games: List<Game>,
    private val onGameClick: (Game) -> Unit
) : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    inner class GameViewHolder(private val binding: ItemGameCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game) {
            binding.apply {
                gameIcon.setImageResource(game.iconResId)
                gameTitle.text = game.title
                gameDescription.text = game.description
                root.setOnClickListener { onGameClick(game) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemGameCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(games[position])
    }

    override fun getItemCount() = games.size
} 