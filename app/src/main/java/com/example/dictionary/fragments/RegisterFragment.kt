package com.example.dictionary.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dictionary.R
import com.example.dictionary.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (validateInput(email, username, password, confirmPassword)) {
                createAccount(email, username, password)
            }
        }
    }

    private fun validateInput(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            binding.emailInputLayout.error = "E-posta adresi gerekli"
            isValid = false
        } else {
            binding.emailInputLayout.error = null
        }

        if (username.isEmpty()) {
            binding.usernameInputLayout.error = "Kullanıcı adı gerekli"
            isValid = false
        } else {
            binding.usernameInputLayout.error = null
        }

        if (password.isEmpty()) {
            binding.passwordInputLayout.error = "Şifre gerekli"
            isValid = false
        } else if (password.length < 6) {
            binding.passwordInputLayout.error = "Şifre en az 6 karakter olmalı"
            isValid = false
        } else {
            binding.passwordInputLayout.error = null
        }

        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordInputLayout.error = "Şifre tekrarı gerekli"
            isValid = false
        } else if (password != confirmPassword) {
            binding.confirmPasswordInputLayout.error = "Şifreler eşleşmiyor"
            isValid = false
        } else {
            binding.confirmPasswordInputLayout.error = null
        }

        return isValid
    }

    private fun createAccount(email: String, username: String, password: String) {
        binding.registerButton.isEnabled = false
        binding.registerButton.text = "Kayıt Yapılıyor..."

        Log.d("RegisterFragment", "Kayıt işlemi başlatılıyor: $email")

        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods ?: emptyList<String>()
                    if (signInMethods.isNotEmpty()) {
                        binding.registerButton.isEnabled = true
                        binding.registerButton.text = "Kayıt Ol"
                        binding.emailInputLayout.error = "Bu e-posta adresi zaten kullanımda"
                        Toast.makeText(context, "Bu e-posta adresi zaten kullanımda", Toast.LENGTH_LONG).show()
                    } else {
                        proceedWithRegistration(email, username, password)
                    }
                } else {
                    binding.registerButton.isEnabled = true
                    binding.registerButton.text = "Kayıt Ol"
                    Toast.makeText(context, "Bir hata oluştu, lütfen tekrar deneyin", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun proceedWithRegistration(email: String, username: String, password: String) {
        // Progress göstergesi
        binding.registerButton.isEnabled = false
        binding.registerButton.text = "Kayıt Yapılıyor..."

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                Log.d("RegisterFragment", "createUserWithEmail:success")
                val user = authResult.user
                user?.let {
                    val userMap = hashMapOf(
                        "uid" to it.uid,
                        "email" to email,
                        "username" to username,
                        "createdAt" to System.currentTimeMillis(),
                        "lastLogin" to System.currentTimeMillis(),
                        "photoUrl" to "",
                        "bio" to "",
                        "points" to 0
                    )
                    
                    Log.d("RegisterFragment", "Kullanıcı bilgileri database'e yazılıyor")
                    
                    usersRef.child(it.uid).setValue(userMap)
                        .addOnSuccessListener {
                            Log.d("RegisterFragment", "Database yazma başarılı")
                            // UI güncellemelerini view'ın yaşam döngüsünü kontrol ederek yapalım
                            view?.post {
                                binding.registerButton.isEnabled = true
                                binding.registerButton.text = "Kayıt Ol"
                                Toast.makeText(
                                    requireContext(), 
                                    "Kayıt başarılı! Giriş yapabilirsiniz.", 
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("RegisterFragment", "Database yazma hatası", e)
                            view?.post {
                                binding.registerButton.isEnabled = true
                                binding.registerButton.text = "Kayıt Ol"
                                Toast.makeText(
                                    requireContext(), 
                                    "Hata: ${e.message}", 
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("RegisterFragment", "createUserWithEmail:failure", e)
                view?.post {
                    binding.registerButton.isEnabled = true
                    binding.registerButton.text = "Kayıt Ol"
                    
                    val errorMessage = when (e.message) {
                        "The email address is already in use by another account." -> 
                            "Bu e-posta adresi zaten kullanımda."
                        "The email address is badly formatted." -> 
                            "Geçersiz e-posta formatı."
                        "Password should be at least 6 characters" -> 
                            "Şifre en az 6 karakter olmalıdır."
                        else -> e.message ?: "Kayıt başarısız."
                    }
                    
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 