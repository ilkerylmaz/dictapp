package com.example.dictionary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dictionary.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.apply {
            sendButton.setOnClickListener {
                val email = emailEditText.text.toString()
                if (validateEmail(email)) {
                    sendResetLink(email)
                }
            }

            backToLoginText.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        if (email.isEmpty()) {
            binding.emailInputLayout.error = "E-posta adresi gerekli"
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInputLayout.error = "Geçerli bir e-posta adresi girin"
            return false
        }
        binding.emailInputLayout.error = null
        return true
    }

    private fun sendResetLink(email: String) {
        // Şifre sıfırlama bağlantısı gönderme işlemi burada yapılacak
        Toast.makeText(
            context,
            "Şifre sıfırlama bağlantısı $email adresine gönderildi",
            Toast.LENGTH_LONG
        ).show()
        
        // İşlem başarılı olduktan sonra giriş sayfasına dön
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 