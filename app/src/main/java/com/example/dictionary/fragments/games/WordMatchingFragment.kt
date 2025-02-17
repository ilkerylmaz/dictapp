package com.example.dictionary.fragments.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dictionary.R
import com.example.dictionary.databinding.FragmentWordMatchingBinding

class WordMatchingFragment : Fragment(R.layout.fragment_word_matching) {
    private var _binding: FragmentWordMatchingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWordMatchingBinding.bind(view)
        // Oyun mantığı buraya gelecek
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 