package com.example.dictionary.repository

import android.content.Context
import android.util.Log
import com.example.dictionary.models.DictionaryTerm
import com.example.dictionary.models.DictionaryResponse
import com.example.dictionary.utils.JsonUtils
import com.google.gson.Gson

class DictionaryRepository(private val context: Context) {
    private val gson = Gson()
    private val fileName = "siber_sozluk.json"
    
    fun getAvailableLetters(): List<Char> {
        // Mevcut terimlerin ilk harflerini al ve sırala
        return getAllTerms()
            .map { it.word.first().uppercaseChar() }
            .distinct()
            .sorted()
    }
    
    fun getAllTerms(): List<DictionaryTerm> {
        return try {
            Log.d("DictionaryRepo", "Reading file: $fileName")
            val jsonString = JsonUtils.getJsonDataFromAsset(context, fileName)
            
            jsonString?.let {
                try {
                    val response: DictionaryResponse = gson.fromJson(it, DictionaryResponse::class.java)
                    Log.d("DictionaryRepo", "Found ${response.terms.size} terms")
                    
                    // Türkçe karakterleri dikkate alarak sırala
                    response.terms.sortedWith(compareBy { 
                        it.word.lowercase().replace('ı', 'i')
                            .replace('ğ', 'g')
                            .replace('ü', 'u')
                            .replace('ş', 's')
                            .replace('ö', 'o')
                            .replace('ç', 'c') 
                    })
                } catch (e: Exception) {
                    Log.e("DictionaryRepo", "Error parsing JSON", e)
                    emptyList()
                }
            } ?: emptyList()
            
        } catch (e: Exception) {
            Log.e("DictionaryRepo", "Error reading dictionary file", e)
            emptyList()
        }
    }
    
    fun getTermsByLetter(letter: Char): List<DictionaryTerm> {
        return getAllTerms().filter { term ->
            term.word.startsWith(letter.toString(), ignoreCase = true)
        }
    }
    
    fun searchTerms(query: String): List<DictionaryTerm> {
        return getAllTerms().filter { term ->
            term.word.contains(query, ignoreCase = true) || 
            term.definition.contains(query, ignoreCase = true)
        }
    }
} 