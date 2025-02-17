package com.example.dictionary.models

import com.google.gson.annotations.SerializedName

data class DictionaryTerm(
    @SerializedName("kavram")
    val word: String,
    @SerializedName("aciklama")
    val definition: String,
    val category: String? = null,
    val example: String? = null
)

data class DictionaryResponse(
    @SerializedName("siberGuvenlikKavramlari")
    val terms: List<DictionaryTerm>
) 