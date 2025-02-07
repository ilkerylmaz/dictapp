package com.example.dictionary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dictionary.R
import com.example.dictionary.databinding.FragmentProfileBinding
import com.example.dictionary.utils.ThemeHelper

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupThemeSwitch()
        setupListeners()
    }

    private fun setupThemeSwitch() {
        val isDarkMode = ThemeHelper.isDarkMode(requireContext())
        binding.themeSettingLayout.setOnClickListener {
            val newMode = !isDarkMode
            ThemeHelper.applyTheme(requireActivity(), newMode)
            updateThemeText(newMode)
        }
        updateThemeText(isDarkMode)
    }

    private fun updateThemeText(isDarkMode: Boolean) {
        binding.themeValueText.text = if (isDarkMode) "Koyu" else "Açık"
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 