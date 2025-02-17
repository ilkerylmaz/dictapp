package com.example.dictionary.utils

import android.content.Context
import android.util.Log
import java.io.IOException

object JsonUtils {
    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        return try {
            Log.d("JsonUtils", "Trying to read file: $fileName")
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            Log.d("JsonUtils", "Successfully read file: $fileName")
            jsonString
        } catch (e: IOException) {
            Log.e("JsonUtils", "Error reading file: $fileName", e)
            null
        }
    }
} 