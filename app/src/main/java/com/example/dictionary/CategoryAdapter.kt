package com.example.dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary.databinding.ItemCategoryBinding
import com.example.dictionary.models.Category

class CategoryAdapter(
    private val categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.apply {
                categoryIcon.setImageResource(category.iconResId)
                categoryTitle.text = category.title
                categoryDescription.text = category.description
                
                // Puan veya sıralama varsa göster
                scoreText.isVisible = category.score != null
                scoreText.text = when (category.id) {
                    1 -> "${category.score} Puan"
                    2 -> "${category.score}. Sırada"
                    else -> ""
                }
                
                root.setOnClickListener { onCategoryClick(category) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount() = categories.size
} 