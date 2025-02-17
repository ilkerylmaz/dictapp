package com.example.dictionary.fragments.games

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.dictionary.R
import com.example.dictionary.databinding.FragmentMemoryGameBinding

class MemoryGameFragment : Fragment(R.layout.fragment_memory_game) {
    private var _binding: FragmentMemoryGameBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMemoryGameBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 