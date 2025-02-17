package com.example.dictionary.fragments.games

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.dictionary.R
import com.example.dictionary.databinding.FragmentWordPuzzleBinding

class WordPuzzleFragment : Fragment(R.layout.fragment_word_puzzle) {
    private var _binding: FragmentWordPuzzleBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWordPuzzleBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 