package com.example.dictionary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary.databinding.ItemDictionaryTermBinding
import com.example.dictionary.models.DictionaryTerm

class DictionaryAdapter(
    private var terms: List<DictionaryTerm>,
    private val onTermClick: (DictionaryTerm) -> Unit
) : RecyclerView.Adapter<DictionaryAdapter.TermViewHolder>() {

    inner class TermViewHolder(private val binding: ItemDictionaryTermBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(term: DictionaryTerm) {
            binding.apply {
                wordText.text = term.word
                definitionText.text = term.definition
                term.example?.let { exampleText.text = "Örnek: $it" }
                root.setOnClickListener { onTermClick(term) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermViewHolder {
        val binding = ItemDictionaryTermBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TermViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TermViewHolder, position: Int) {
        holder.bind(terms[position])
    }

    override fun getItemCount() = terms.size

    fun updateTerms(newTerms: List<DictionaryTerm>) {
        terms = newTerms
        notifyDataSetChanged()
    }
} 