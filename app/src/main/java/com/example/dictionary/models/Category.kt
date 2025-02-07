package com.example.dictionary.models

data class Category(
    val id: Int,
    val title: String,
    val description: String,
    val iconResId: Int,
    val score: Int? = null // Puan gösterimi için
) 