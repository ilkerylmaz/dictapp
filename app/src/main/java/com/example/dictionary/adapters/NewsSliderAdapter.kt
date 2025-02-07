package com.example.dictionary.adapters

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dictionary.R
import com.example.dictionary.databinding.ItemNewsSliderBinding
import com.example.dictionary.models.News

class NewsSliderAdapter(
    private val newsList: List<News>,
    private val onNewsClick: (News) -> Unit
) : RecyclerView.Adapter<NewsSliderAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(private val binding: ItemNewsSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(news: News) {
            binding.apply {
                newsTitle.text = news.title
                newsDescription.text = news.description
                
                Log.d("NewsAPI", "Loading image URL: ${news.imageUrl}")
                
                Glide.with(newsImage.context)
                    .load(news.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.loading_placeholder)
                    .error(R.drawable.error_placeholder)
                    .centerCrop()
                    .into(newsImage)

                root.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
                    root.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsSliderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount() = newsList.size
} 